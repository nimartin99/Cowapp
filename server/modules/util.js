/**
 * Util file for helper-functions used in the 'server.js' file.
 *
 * @author Mergim Miftari
 * @author Philipp Alessandrini
 * @version 2020-11-25
 */

/**
 * Increases the given Key according to our conditions.
 *
 * @param key the current key
 * @return the increased key
 */
exports.calculateNewKey = function (key) {
    let newKey = key.split("-").join(""); // removes the -
    let chars = newKey.split('');
    let i = (chars.length - 1);

    chars = increaseChars(chars, i);

    let result = chars.join('');

    result = addChar(result, '-', 4);
    result = addChar(result, '-', 9);
    result = addChar(result, '-', 14);

    return result;
}

/**
 * Inits all relevant direct or indirect contact keys as an array
 *
 * @param keyCollection, direct or indirect key collection
 * @param currentKeyList all current keys
 * @return key[], all relevant direct or indirect keys
 */
exports.pushKeyArray = function (keyCollection, currentKeyList) {
    let keys = [];

    let i;
    let j;
    for (i = 0; i < keyCollection.length; i++) {
        for (j = 0; j < keyCollection[i].length; j++) {
            currentKeyList = keyCollection[i];
            keys.push(currentKeyList[j]);
        }
    }

    return keys;
}

// --- helper functions ---

function addChar(str, ch, position) {
    let len = str.length;
    let updatedArr = [];
    let getChars;

    getChars = str.substring(0, position).split("");

    pushChars(getChars, updatedArr);

    updatedArr[position] = ch;
    getChars = str.substring(position, len).split("");

    pushChars(getChars, updatedArr);

    return updatedArr.join("");
}

function increaseChars(chars, position) {
    if (position === -1) {
        return chars;
    }

    let charot = chars;

    if (getNextChar(charot[position]) === "0") {
        charot[position] = "0";
        charot = increaseChars(charot, position - 1);
    } else {
        charot[position] = getNextChar(charot[position]);
    }

    return charot;
}

function getNextChar(chi) {
    switch (chi) {
        case "0": return "1";
        case "1": return "2";
        case "2": return "3";
        case "3": return "4";
        case "4": return "5";
        case "5": return "6";
        case "6": return "7";
        case "7": return "8";
        case "8": return "9";
        case "9": return "a";
        case "a": return "b";
        case "b": return "c";
        case "c": return "d";
        case "d": return "e";
        case "e": return "f";

        default: return "0";
    }
}

function pushChars(getChars, updatedArr) {
    let i;
    for (i = 0; i < getChars.length; i++) {
        updatedArr.push(getChars[i]);
    }
}