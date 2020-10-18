/**
 * Server for CoWApp
 *
 * @author Mergim Miftari
 * @author Philipp Alessandrini
 * @version 2020-10-17
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
        const cowappDb = db.db('CoWAppDB');
        const keyCollection = cowappDb.collection('key');
        // request key from mongodb
        app.get('/request_key', (req, res) => {
            keyCollection.findOne({}, (err, result) => {
                if (result != null) {
                    const requestedKey = { key: result.key };
                    // send key to the client
                    res.status(200).send(JSON.stringify(requestedKey));
                } else {
                    // object not found
                    res.status(404).send();
                }
            });
        });
        // send key to mongodb
        app.post('/send_key', (req, res) => {
            keyCollection.findOne({}, (err, result) => {
                if (result != null) {
                    const sentKey = { $set: {key: req.body.key} };
                    // replace it with new one
                    keyCollection.updateOne({}, sentKey, (err, result) => {
                        res.status(200).send();
                    })
                } else {
                    // object not found
                    res.status(404).send();
                }
            })
        });
    }
});

// start listening on port 3000
app.listen(3000, () => {
    console.log("Listening on port 3000...");
})