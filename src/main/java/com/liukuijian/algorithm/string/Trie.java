package com.liukuijian.algorithm.string;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典树
 */

public class Trie {
    public Map<Character,Trie> next;
    public Character c;
    public boolean isWord;
    /** Initialize your data structure here. */
    public Trie() {
        next = new HashMap<>();
    }

    /** Inserts a word into the trie. */
    public void insert(String word) {
        char[] cs = word.toCharArray();
        Map<Character, Trie> tmp = next;//防止修改本地map
        for(int i = 0; i < cs.length; i++){
            if(!tmp.containsKey(cs[i])){
                Trie trie = new Trie();
                trie.c = cs[i];
                if(i == cs.length - 1){
                    trie.isWord = true;
                }
                tmp.put(cs[i],trie);
                tmp = trie.next;
            }else{
                Trie otherTrie = tmp.get(cs[i]);
                tmp = otherTrie.next;
                if(i == cs.length - 1){
                    otherTrie.isWord = true;
                }
            }
        }
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        char[] cs = word.toCharArray();
        Map<Character, Trie> tmp = next;
        for(int i = 0; i < cs.length; i++){
            if(!tmp.containsKey(cs[i])){
                return false;
            }else{
                Trie otherTrie = tmp.get(cs[i]);
                tmp = otherTrie.next;
                if(i == cs.length - 1 && otherTrie.isWord == true){
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        char[] cs = prefix.toCharArray();
        Map<Character, Trie> tmp = next;
        for(int i = 0; i < cs.length; i++){
            if(!tmp.containsKey(cs[i])){
                return false;
            }else{
                Trie otherTrie = tmp.get(cs[i]);
                tmp = otherTrie.next;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        Trie obj = new Trie();
        obj.insert("apple");
        boolean param_2 = obj.search("app");
        boolean param_3 = obj.startsWith("app");

    }
}
