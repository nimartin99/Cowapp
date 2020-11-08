/**
 * Server for CoWApp
 *
 * @author Mergim Miftari
 * @author Philipp Alessandrini
 * @version 2020-11-04
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
        const infectedCollection = cowappDb.collection('infected');
        const keyPairsCollection = cowappDb.collection('key_pairs');
        // request a key from mongodb
        app.get('/request_key', (req, res) => {
            keyCollection.findOne({}, (err, result) => {
                if (result != null) {
                    // get current key value from mongodb
                    const oldKey = parseInt(result.key);
                    // calculate a new key
                    const newKey = (oldKey + 1).toString();
                    // send new key to mongodb
                    const sendKey = { $set: {key: newKey} };
                    // check if current key is infected
                    infectedCollection.findOne({key: oldKey.toString()}, function (err, result) {
                        if (err) throw err;
                        if (result != null) {
                            // update new key
                            infectedCollection.updateOne(result, sendKey);
                        }
                    });
                    // check if current key has had contacts
                    keyPairsCollection.findOne({key: oldKey.toString()}, function (err, result) {
                        if (err) throw err;
                        if (result != null) {
                            // update new key
                            keyPairsCollection.updateOne(result, sendKey);
                        }
                        // also check if current key is a contact
                        keyPairsCollection.findOne({contactKey: oldKey.toString()}, function (err, result) {
                            if (err) throw err;
                            if (result != null) {
                                // update new key
                                keyPairsCollection.updateOne({contactKey: oldKey.toString()},
                                    { $set: {'contactKey.$': newKey} });
                            }
                        });
                    });
                    // also update new unique key in mongodb
                    keyCollection.updateOne({}, sendKey, (err, result) => {
                        if (err) throw err;
                        // send key to the client
                        const requestedKey = { key: newKey };
                        res.status(200).send(JSON.stringify(requestedKey));
                    });
                } else {
                    // object not found
                    res.status(404).send();
                }
            });
        });
        // send infected key to mongodb
        app.post('/report_infection', (req, res) => {
            // delete reported key after 14 days
            infectedCollection.createIndex({ "createdAt": 1 }, { expireAfterSeconds: 14*24*60*60 });
            // delete key pairs after 21 days
            keyPairsCollection.createIndex({ "createdAt": 1 }, { expireAfterSeconds: 21*24*60*60 });
            // pack a userReport const which will be sent to mongodb
            const userReport = {
                "createdAt": new Date(),
                key: req.body.key
            };
            const searchKey = { key: userReport.key };
            // search in the db if userKey is already existing
            infectedCollection.findOne(searchKey, (err, result) => {
                // add infected key if it's not already reported
                if (result == null) {
                    infectedCollection.insertOne(userReport, (err, result) => {
                        if (err) throw err;
                        // check if user also has had contacts and add contacts
                        if (typeof req.body.contactDate !== 'undefined') {
                            const contactReport = {
                                "createdAt": new Date(),
                                key: req.body.key,
                                contactDate: req.body.contactDate.split("|"),
                                contactKey: req.body.contactKey.split("|")
                            }
                            keyPairsCollection.insertOne(contactReport, (err, result) => {
                                if (err) throw err;
                            });
                        }
                        res.status(200).send();
                    });
                } else {
                    // user has infection already reported
                    res.status(400).send();
                }
            });
        });
    }
});

// start listening on port 3000
app.listen(3000, () => {
    console.log("Listening on port 3000...");
});