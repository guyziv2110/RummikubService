/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikub.service.ws.utils;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author guy
 */
public class RummikubUniqueNameGenerator {
    
    private static RummikubUniqueNameGenerator instance = new RummikubUniqueNameGenerator();

    //make the constructor private so that this class cannot be
    //instantiated
    private RummikubUniqueNameGenerator(){}

    //Get the only object available
    public static RummikubUniqueNameGenerator getInstance(){
       return instance;
    }
    final String computerLexicon = "1234567890";
    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    final java.util.Random rand = new java.util.Random();

    // consider using a Map<String,Boolean> to say whether the identifier is being used or not 
    final Set<String> identifiers = new HashSet<String>();

    public String computerRandomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            builder.append("Computer #");
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++)
                builder.append(computerLexicon.charAt(rand.nextInt(computerLexicon.length())));
            if(identifiers.contains(builder.toString())) 
                builder = new StringBuilder();
        }
        return builder.toString();        
    }
    
    public String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++)
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            if(identifiers.contains(builder.toString())) 
                builder = new StringBuilder();
        }
        return builder.toString();
    }    
}
