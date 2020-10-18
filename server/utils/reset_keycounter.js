/**
 * Util-Script which resets the Key-Counter to 0.
 *
 * @author Mergim Miftari
 * @author Philipp Alessandrini
 * @version 2020-10-18
 */

// init mongoDB
const MongoClient = require('mongodb').MongoClient;
const url = "mongodb+srv://admin:zyI8ZX5zmAyfjaVt@cowapp.9hh4n.mongodb.net/CoWApp?retryWrites=true&w=majority";

// connect to the database
MongoClient.connect(url, { useUnifiedTopology: true }, function(err, db) {
    if (err) {
        console.log("Error while connecting mongo client");
    } else {
        const cowappDb = db.db('CoWAppDB');
        const keyCollection = cowappDb.collection('key');
        // reset key-counter
        const zerocounter = { $set: {key: "0"} };
        keyCollection.updateOne({}, zerocounter, function(err, res) {
            if (err) throw err;
            console.log("Key-Counter successfully reset");
            db.close();
        });
    }
});