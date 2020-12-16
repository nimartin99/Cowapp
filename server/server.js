/**
 * Server for CoWApp
 *
 * @author Mergim Miftari
 * @author Philipp Alessandrini
 * @version 2020-12-15
 */

// init web framework
const express = require('express');
const app = express();
// init mongoDB
const MongoClient = require('mongodb').MongoClient;
const url = "mongodb+srv://admin:zyI8ZX5zmAyfjaVt@cowapp.9hh4n.mongodb.net/CoWApp?retryWrites=true&w=majority";
// init own util module
const util = require('./modules/util');
// enable json parsing
app.use(express.json());

// connect to the database
MongoClient.connect(url, { useUnifiedTopology: true }, function(err, db) {
    if (err) {
        console.log("Error while connecting mongo client");
    } else {
        // define db and collections
        const cowappDb = db.db('CoWAppDB');
        const keyCollection = cowappDb.collection('key');
        const directContactsCollection = cowappDb.collection('direct_contacts');
        const indirectContactsCollection = cowappDb.collection('indirect_contacts');
        const infectionIdsCollection = cowappDb.collection('infection_ids');
        const nonInfectionIdsCollection = cowappDb.collection('non_infection_ids');

        // request a key from mongodb
        app.get('/request_key', (req, res) => {
            keyCollection.findOne({}, (err, result) => {
                if (err) throw err;
                // calculate new key
                let newKey = util.calculateNewKey(result.key);
                let keyToUpdate = { $set: {key: newKey} }
                // find and update new unique key in mongodb
                keyCollection.updateOne({}, keyToUpdate, (err, result) => {
                    if (err) throw err;
                    // send key to the client
                    res.status(200).send(JSON.stringify(newKey));
                });
            });
        });

        // send infected key to mongodb
        app.post('/report_infection', (req, res) => {
            // delete contact keys collections after 21 days
            directContactsCollection.createIndex({ "createdAt": 1 }, { expireAfterSeconds: 3*24*60*60 });
            indirectContactsCollection.createIndex({ "createdAt": 1 }, { expireAfterSeconds: 3*24*60*60 });
            // check if infected user has had contacts
            if (typeof req.body.contactType !== 'undefined') {
                // pack a directContactReport const which will be sent to mongodb
                const contactType = { contactType: req.body.contactType }
                const contactReport = {
                    "createdAt": new Date(),
                    contactDate: req.body.contactDate.split("|"),
                    contactKey: req.body.contactKey.split("|")
                }
                // if it's a direct contact
                if (contactType.contactType === "DIRECT") {
                    directContactsCollection.insertOne(contactReport, (err, result) => {
                        if (err) throw err;
                        res.status(200).send();
                    });
                } else if (contactType.contactType === "INDIRECT") { // if it's a indirect contact
                    indirectContactsCollection.insertOne(contactReport, (err, result) => {
                        if (err) throw err;
                        res.status(200).send();
                    });
                } else {
                    res.status(400).send();
                }
            } else {
                res.status(404).send();
            }
        });

        // send user keys to mongodb
        app.post('/request_infection_status', (req, res) => {
            // check if user has keys
            if (typeof req.body.userDate !== 'undefined') {
                // get all user keys from the client
                const userReport = {
                    userDate: req.body.userDate.split("|"),
                    userKey: req.body.userKey.split("|")
                }
                // init all relevant direct and indirect contact keys as an array
                let directKeyCollections = [];
                let directKeys = [];
                let indirectKeyCollections = [];
                let indirectKeys = [];
                // count the number of direct and indirect contacts
                let directContactNbr = 0;
                let indirectContactNbr = 0;
                // check if direct contacts collection contains one user key
                directContactsCollection.find({ contactKey: {$in: userReport.userKey}}, async function(err, result) {
                    if (err) throw err;
                    // check if user has had direct contact to an infected person and count the contacts
                    await result.forEach(function(key, keyIndex) {
                        directContactNbr++;
                        directKeyCollections.push(key.contactKey);
                    });
                    // init variables to sum up the collections as one array
                    let currentKeyList = [];
                    // user has had direct contact
                    if (directContactNbr > 0) {
                        // init all relevant direct contact keys as an array
                        directKeys = util.pushKeyArray(directKeyCollections, currentKeyList);
                        // calculate identical values
                        let intersectionDirectKeys = (userReport.userKey).filter(x => directKeys.includes(x));
                        // use the intersection to calculate the number of non infected keys in userReport
                        let notInfectedKeysNbr = (userReport.userKey).length - intersectionDirectKeys.length;
                        // create a status report with infection status, contact persons and number of not infected keys
                        const directInfectionStatus = {
                            status: "DIRECT_CONTACT",
                            contactNbr: directContactNbr.toString(),
                            lastInfectionTime: notInfectedKeysNbr.toString()
                        }
                        // delete all user keys in direct_contacts - collection
                        await directContactsCollection.updateMany(
                            { }, // all id's
                            {$pull: {contactKey: {$in: userReport.userKey}}}
                        )
                        // send response
                        res.status(200).send(JSON.stringify(directInfectionStatus));
                    } else { // else check if user has had indirect contact
                        indirectContactsCollection.find({ contactKey: {$in: userReport.userKey}}, async function(err, result) {
                            if (err) throw err;
                            // check if user has had direct contact to an infected person and count the contacts
                            await result.forEach(function(key, keyIndex) {
                                indirectContactNbr++;
                                indirectKeyCollections.push(key.contactKey);
                            });
                            // user has had indirect contact
                            if (indirectContactNbr > 0) {
                                // init all relevant indirect contact keys as an array
                                indirectKeys = util.pushKeyArray(indirectKeyCollections, currentKeyList);
                                // calculate identical values
                                let intersectionIndirectKeys = (userReport.userKey).filter(x => indirectKeys.includes(x));
                                // use the intersection to calculate the number of non infected keys in userReport
                                let notInfectedKeysNbr = (userReport.userKey).length - intersectionIndirectKeys.length;
                                // create a status report with infection status, contact persons and number of not infected keys
                                const indirectInfectionStatus = {
                                    status: "INDIRECT_CONTACT",
                                    contactNbr: indirectContactNbr.toString(),
                                    lastInfectionTime: notInfectedKeysNbr.toString()
                                }
                                // delete all user keys in indirect_contacts - collection
                                await indirectContactsCollection.updateMany(
                                    { }, // all id's
                                    {$pull: {contactKey: {$in: userReport.userKey}}}
                                )
                                // send response
                                res.status(200).send(JSON.stringify(indirectInfectionStatus));
                            } else { // user has had no contact to an infected person
                                res.status(400).send();
                            }
                        });
                    }
                });
            } else {
                res.status(404).send();
            }
        });

        // send pin id to mongodb
        app.post('/send_id', (req, res) => {
            // delete ids after some time
            infectionIdsCollection.createIndex({ "createdAt": 1 }, { expireAfterSeconds: 3*24*60*60 });
            nonInfectionIdsCollection.createIndex({ "createdAt": 1 }, { expireAfterSeconds: 3*24*60*60 });
            // check if id is reported
            if (typeof req.body.id !== 'undefined') {
                // pack a id report const which will be sent to mongodb
                const infection = { infection: req.body.infection }
                const idReport = {
                    "createdAt": new Date(),
                    id: req.body.id
                }
                // if it's an infection report
                if (infection.infection === "TRUE") {
                    infectionIdsCollection.insertOne(idReport, (err, result) => {
                        if (err) throw err;
                        res.status(200).send();
                    });
                } else if (infection.infection === "FALSE") { // if it's a non infection report
                    nonInfectionIdsCollection.insertOne(idReport, (err, result) => {
                        if (err) throw err;
                        res.status(200).send();
                    });
                } else {
                    res.status(400).send();
                }
            } else {
                res.status(404).send();
            }
        });

        // verify pin id in mongodb
        app.post('/verify_id', (req, res) => {
            // check if id ist reported
            if (typeof req.body.id !== 'undefined') {
                // pack a id report const which will be sent to mongodb
                const infection = { infection: req.body.infection }
                const idToCheck = { id: req.body.id }
                // if it's an infection report
                if (infection.infection === "TRUE") {
                    infectionIdsCollection.deleteOne(idToCheck)
                        .then(result => {
                            if (result.deletedCount > 0) {
                                res.status(200).send();
                            } else {
                                res.status(404).send();
                            }
                        }).catch(err => console.error(`Delete failed with error: ${err}`));
                } else if (infection.infection === "FALSE") { // if it's a non infection report
                    nonInfectionIdsCollection.deleteOne(idToCheck)
                        .then(result => {
                            if (result.deletedCount > 0) {
                                res.status(200).send();
                            } else {
                                res.status(404).send();
                            }
                        }).catch(err => console.error(`Delete failed with error: ${err}`));
                } else {
                    res.status(400).send();
                }
            } else {
                res.status(404).send();
            }
        });
    }
});

// start listening on port 3000
app.listen(3000, () => {
    console.log("Listening on port 3000...");
});