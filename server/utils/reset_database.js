/**
 * Util-Script which resets the Key-Counter to 0.
 *
 * @author Mergim Miftari
 * @author Philipp Alessandrini
 * @version 2020-10-29
 */

// init mongoDB
const MongoClient = require('mongodb').MongoClient;
const url = "mongodb+srv://admin:zyI8ZX5zmAyfjaVt@cowapp.9hh4n.mongodb.net/CoWApp?retryWrites=true&w=majority";

// connect to the database
MongoClient.connect(url, { useUnifiedTopology: true }, async function(err, db) {
    if (err) {
        console.log("Error while connecting mongo client");
    } else {
        // define db and collections which are going to be reset
        const cowappDb = db.db('CoWAppDB');
        const keyCollection = cowappDb.collection('key');
        const infectedCollection = cowappDb.collection('infected');
        const keyPairsCollection = cowappDb.collection('key_pairs');
        // reset key-counter in 'key' collection
        const zerocounter = { $set: {key: "0"} };
        await keyCollection.updateOne({}, zerocounter, function(err, res) {
            if (err) throw err;
            console.log("Key-Counter successfully reset");
        });
        // drop 'infected' collection
        await infectedCollection.drop(function(err, delOK) {
            if (err) console.log("There are no infected keys reported at the moment");
            if (delOK) console.log("Infected keys successfully deleted");
        });
        // drop 'key_pairs' collection
        await keyPairsCollection.drop(function(err, delOK) {
            if (err) console.log("There are no key pairs reported at the moment");
            if (delOK) console.log("Key pairs successfully deleted");
            db.close();
        });
    }
});