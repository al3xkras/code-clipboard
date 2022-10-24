"use strict";

class SuffixTree {}

SuffixTree.prototype.create = function (some,obj){
    SuffixTree.prototype.clear(some)
    if (obj){
        some.text=obj.text
        some.nextStrIndex = obj.nextStrIndex;
        some.delimiters = obj.delimiters;

        some.root = obj.root;
        some.bottom = obj.bottom;
        some.root.suffixLink = obj.root.suffixLink;

        some.s = obj.s;
        some.k = obj.k;
        some.i = obj.i;
    }
    return some
}

SuffixTree.prototype.clear = function (some){
    some.text = '';

    some.nextStrIndex = 0;
    some.delimiters = [];

    some.root = Node.prototype.create({});
    some.bottom = Node.prototype.create({});
    some.root.suffixLink = some.bottom;

    some.s = some.root;
    some.k = 0;
    some.i = -1;
}

SuffixTree.prototype.addString=function(some,str){
    str = str.replace(/[^a-zA-Z0-9]/g, '').trim();

    if (str.length === 0) {
        return;
    }

    const sep = '#' + some.nextStrIndex + '#';
    str += sep;
    some.nextStrIndex++;

    const temp = some.text.length;
    some.text += str.toLowerCase();
    some.delimiters.push(sep);
    let s, k, i;
    s = some.s;
    k = some.k;
    i = some.i;

    for (let j = temp; j < some.text.length; j++) {
        Node.prototype.addTransition(some.bottom, some.root, j, j, some.text[j])
    }

    while (some.text[i + 1]) {
        i++;
        let up = SuffixTree.prototype.update(some, s, k, i);
        up = SuffixTree.prototype.canonize(some, up[0], up[1], i);
        s = up[0];
        k = up[1];
    }

    some.s = s;
    some.k = k;
    some.i = i;
    return some;
}

SuffixTree.prototype.update=function (some,s,k,i) {
    let oldr = some.root;
    let endAndr = SuffixTree.prototype.testAndSplit(some, s, k, i - 1, some.text[i]);
    let endPoint = endAndr[0];
    let r = endAndr[1];

    while (!endPoint) {
        Node.prototype.addTransition(r, {}, i, Infinity, some.text[i])

        if (oldr !== some.root) {
            oldr.suffixLink = r;
        }

        oldr = r;
        const sAndk = SuffixTree.prototype.canonize(some, s.suffixLink, k, i - 1);
        s = sAndk[0];
        k = sAndk[1];
        endAndr = SuffixTree.prototype.testAndSplit(some, s, k, i - 1, some.text[i]);
        endPoint = endAndr[0];
        r = endAndr[1];
    }

    if (oldr !== some.root) {
        oldr.suffixLink = s;
    }

    return [s, k];
}

SuffixTree.prototype.testAndSplit=function(some, s, k, p, t) {
    if (k <= p) {
        const traNs = s.transitions[some.text[k]];
        const s2 = traNs[0],
            k2 = traNs[1],
            p2 = traNs[2];
        if (t === some.text[k2 + p - k + 1]) {
            return [true, s];
        } else {
            const r = Node.prototype.create({});
            Node.prototype.addTransition(s, r, k2, k2 + p - k, some.text[k2]);
            Node.prototype.addTransition(r, s2, k2 + p - k + 1, p2, some.text[k2 + p - k + 1]);
            return [false, r];
        }
    } else {
        if (!s.transitions[t]) return [false, s];
        else return [true, s];
    }
}

SuffixTree.prototype.canonize=function(some, s, k, p) {
    if (p < k) return [s, k];
    else {
        let traNs = s.transitions[some.text[k]];
        let s2 = traNs[0],
            k2 = traNs[1],
            p2 = traNs[2];

        while (p2 - k2 <= p - k) {
            k = k + p2 - k2 + 1;
            s = s2;

            if (k <= p) {
                traNs = s.transitions[some.text[k]];
                s2 = traNs[0];
                k2 = traNs[1];
                p2 = traNs[2];
            }
        }

        return [s, k];
    }
}

SuffixTree.prototype.search = function(some, pattern, skip, count) {
    pattern = pattern.toLowerCase();

    let matchedWordIds = [];

    let curNode = some.root;
    let curPatternIndex = 0;

    while (curNode != null) {
        let selectedTrans = null;
        for (let key in curNode.transitions) {
            if (key === pattern[curPatternIndex]) {
                selectedTrans = curNode.transitions[key];
                break;
            }
        }

        if (selectedTrans == null) {
            return [];
        }

        let textIndex = selectedTrans[1];
        for (; textIndex <= Math.min(selectedTrans[2], some.text.length - 1); textIndex++) {
            if (some.text[textIndex] === pattern[curPatternIndex]) {
                curPatternIndex++;
                if (curPatternIndex >= pattern.length) {
                    break;
                }
            } else {
                return [];
            }
        }

        if (curPatternIndex >= pattern.length) {
            matchedWordIds = SuffixTree.prototype.selectWordsUnder(some,selectedTrans, skip, count);
            curNode = null;
        } else {
            curNode = selectedTrans[0];
        }
    }

    return matchedWordIds;
}

SuffixTree.prototype.selectWordsUnder = function(some,transition, skip, count) {
    let frontier = [
        {
            start: -1,
            end: -1,
            prefixCount: 0,
            transition,
            root: transition,
        },
    ];

    let skipped=0
    let found=0
    const matchedWordIds = new Set();

    while (frontier.length > 0) {
        const curElement = frontier.pop();

        let textIndex = curElement.transition[1];
        for (
            ;
            textIndex <= Math.min(curElement.transition[2], some.text.length - 1);
            textIndex++
        ) {
            if (some.text[textIndex] === '#') {
                if (curElement.start < 0) {
                    curElement.start = textIndex + 1;
                } else {
                    curElement.end = textIndex;

                    const matchedWordId = +some.text.slice(curElement.start - curElement.prefixCount, curElement.end);
                    if (skip>0 && skipped<skip){
                        skipped++
                    } else {
                        matchedWordIds.add(matchedWordId);
                        found++
                        if (count>0 && found>=count){
                            return [...matchedWordIds];
                        }
                    }

                    break;
                }
            }
        }

        if (curElement.end < 0) {
            for (const key in curElement.transition[0].transitions) {
                frontier.push({
                    start:
                        curElement.start >= 0
                            ? curElement.transition[0].transitions[key][1]
                            : -1,
                    end: -1,
                    prefixCount:
                        curElement.start >= 0
                            ? curElement.prefixCount + Math.min(curElement.transition[2], some.text.length - 1) -
                            curElement.start + 1
                            : 0,
                    transition: curElement.transition[0].transitions[key],
                    root: curElement.root,
                });
            }
        }
    }
    return [...matchedWordIds];
}

class Node {}

Node.prototype.create=function(some){
    some.transitions = [];
    some.suffixLink = null;
    return some;
}

Node.prototype.addTransition=function(some, node, start, end, t){
    some.transitions[t] = [node, start, end];
}

Node.prototype.isLeaf=function(some){
    return Object.keys(some.transitions).length === 0;
}