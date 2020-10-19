/**
 * Server for CoWApp
 *
 * @author Mergim Miftari
 * @author Philipp Alessandrini
 * @version 2020-10-18
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
        // request a key from mongodb
        app.get('/request_key', (req, res) => {
            keyCollection.findOne({}, (err, result) => {
                if (result != null) {
                    // get current key value from mongodb
                    const oldKey = parseInt(result.key);
                    // calculate a new key
                    const newKey = (oldKey + 1).toString();
                    // send new key to mongodb
                    const sendKey = { $set: {key : newKey} };
                    // update new key in mongodb
                    keyCollection.updateOne({}, sendKey, (err, result) => {
                        if (err) throw err;
                        // send key to the client
                        const requestedKey = { key: newKey };
                        res.status(200).send(JSON.stringify(requestedKey));
                    })
                } else {
                    // object not found
                    res.status(404).send();
                }
            });
        });
    }
});

// start listening on port 3000
app.listen(3000, () => {
    console.log("Listening on port 3000...");
})