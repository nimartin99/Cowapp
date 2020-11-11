/**
 * Util-Script which resets the Key-Counter to 0.
 *
 * @author Mergim Miftari
 * @author Philipp Alessandrini
 * @version 2020-11-11
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
        const directContactsCollection = cowappDb.collection('direct_contacts');
        const indirectContactsCollection = cowappDb.collection('indirect_contacts');
        // reset key value in 'key' collection
        const zerokey = { $set: {key: "0000-0000-0000-000000000000"} };
        await keyCollection.updateOne({}, zerokey, function(err, res) {
            if (err) throw err;
            console.log("Key value successfully reset");
        });
        // drop 'directContactsCollection' collection
        await directContactsCollection.drop(function(err, delOK) {
            if (err) console.log("There are no direct contact keys reported at the moment");
            if (delOK) console.log("Direct contact keys successfully deleted");
        });
        // drop 'indirectContactsCollection' collection
        await indirectContactsCollection.drop(function(err, delOK) {
            if (err) console.log("There are no indirect contact keys reported at the moment");
            if (delOK) console.log("Indirect contact keys successfully deleted");
            db.close();
        });
    }
});