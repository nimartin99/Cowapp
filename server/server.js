/**
 * Server for CoWApp
 *
 * @author Mergim Miftari
 * @author Philipp Alessandrini
 * @version 2020-11-12
 */

// init web framework
const express = require('express');
const app = express();
// init mongoDB
const MongoClient = require('mongodb').MongoClient;
const url = "mongodb+srv://admin:zyI8ZX5zmAyfjaVt@cowapp.9hh4n.mongodb.net/CoWApp?retryWrites=true&w=majority";
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

        // request a key from mongodb
        app.get('/request_key', (req, res) => {
            keyCollection.findOne({}, (err, result) => {
                if (err) throw err;
                // get current key value from mongodb
                const requestedKey = { key: result.key };
                // send key to the client
                res.status(200).send(JSON.stringify(requestedKey));
            });
        });
        // send new key to mongodb
        app.post('/send_key', (req, res) => {
            const newKey = req.body.key;
            const keyToUpdate = { $set: {key: newKey} }
            // find and update new unique key in mongodb
            keyCollection.updateOne({}, keyToUpdate, (err, result) => {
                if (err) throw err;
                // send key to the client
                res.status(200).send();
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
                // count the number of direct and indirect contacts
                let directContactNbr = 0;
                let indirectContactNbr = 0;
                // check if direct contacts collection contains one user key
                directContactsCollection.find({ contactKey: {$in: userReport.userKey}}, async function(err, result) {
                    if (err) throw err;
                    // check if user has had direct contact to an infected person and count the contacts
                    await result.forEach(function(key, keyIndex) {
                        directContactNbr++;
                    });
                    // user has had direct contact
                    if (directContactNbr > 0) {
                        res.status(200).send(JSON.stringify("DIRECT_CONTACT"));
                    } else { // else check if user has had indirect contact
                        indirectContactsCollection.find({ contactKey: {$in: userReport.userKey}}, async function(err, result) {
                            if (err) throw err;
                            // check if user has had direct contact to an infected person and count the contacts
                            await result.forEach(function(key, keyIndex) {
                                indirectContactNbr++;
                            });
                            // user has had indirect contact
                            if (indirectContactNbr > 0) {
                                res.status(200).send(JSON.stringify("INDIRECT_CONTACT"));
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
    }
});

// start listening on port 3000
app.listen(3000, () => {
    console.log("Listening on port 3000...");
});