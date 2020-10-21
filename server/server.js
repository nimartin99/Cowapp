/**
 * Server for CoWApp
 *
 * @author Mergim Miftari
 * @author Philipp Alessandrini
 * @version 2020-10-21
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
                    // check if current key is infected and update infected key
                    infectedCollection.findOne({key: oldKey.toString()}, function (err, result) {
                        if (err) throw err;
                        if (result != null) {
                            // update new key
                            infectedCollection.updateOne(result, sendKey);
                        }
                    });
                    // update new key in mongodb
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
            // get key from user
            const userReport = {
                date: req.body.date,
                key: req.body.key
            };
            const searchKey = { key: userReport.key };
            // search in the db if key is already existing
            infectedCollection.findOne(searchKey, (err, result) => {
                // add infected key if its not existing
                if (result == null) {
                    infectedCollection.insertOne(userReport, (err, result) => {
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