package com.eulerity.hackathon.services;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class OpenNLPService {
    // refernce: henjunan.top/detail/Detail?id=7
    // models: https://opennlp.sourceforge.net/models-1.5/

    private Tokenizer tokenizer;
    private NameFinderME personNameFinder;  // for person name recognition
    private NameFinderME orgnizationNameFinder;  // for organization recognition
    private NameFinderME locationNameFinder;  // for location recognition

    private ServletContext context;

    public OpenNLPService(ServletContext context) throws Exception {
        this.context = context;
        String tokenizerModelPath = context.getRealPath("/WEB-INF/models/en-token.bin");
        String personNameFinderModelPath = context.getRealPath("/WEB-INF/models/en-ner-person.bin");
        String organizationNameFinderModelPath = context.getRealPath("/WEB-INF/models/en-ner-organization.bin");
        String locationNameFinderModelPath = context.getRealPath("/WEB-INF/models/en-ner-location.bin");


        // Load tokenizer model
        try (InputStream modelIn = new FileInputStream(tokenizerModelPath)) {
            TokenizerModel tokModel = new TokenizerModel(modelIn);
            tokenizer = new TokenizerME(tokModel);
            System.out.println(1);
        }

        // Load person named entity recognizer model
        try (InputStream modelIn = new FileInputStream(personNameFinderModelPath)) {
            TokenNameFinderModel nerModel1 = new TokenNameFinderModel(modelIn);
            personNameFinder = new NameFinderME(nerModel1);
        }

        try (InputStream modelIn = new FileInputStream(organizationNameFinderModelPath)) {
            TokenNameFinderModel nerModel2 = new TokenNameFinderModel(modelIn);
            orgnizationNameFinder = new NameFinderME(nerModel2);
        }

        try (InputStream modelIn = new FileInputStream(locationNameFinderModelPath)) {
            TokenNameFinderModel nerModel3 = new TokenNameFinderModel(modelIn);
            locationNameFinder = new NameFinderME(nerModel3);
        }
    }

    public List<String> extractPersonEntities(String alt){
        String[] tokens = tokenizer.tokenize(alt);
        Span[] spans = personNameFinder.find(tokens);
        List<String> entities = new ArrayList<>();

        for(Span span:spans){
            StringBuilder sb = new StringBuilder();
            for(int i=span.getStart(); i< span.getEnd(); i++){
                sb.append(tokens[i]).append(" ");
            }
            entities.add(sb.toString().trim());
        }
        personNameFinder.clearAdaptiveData();
        return entities;
    }

    public List<String> extractOrganizationEntities(String alt){
        String[] tokens = tokenizer.tokenize(alt);
        Span[] spans = orgnizationNameFinder.find(tokens);
        List<String> entities = new ArrayList<>();

        for(Span span:spans){
            StringBuilder sb = new StringBuilder();
            for(int i=span.getStart(); i< span.getEnd(); i++){
                sb.append(tokens[i]).append(" ");
            }
            entities.add(sb.toString().trim());
        }
        orgnizationNameFinder.clearAdaptiveData();
        return entities;
    }

    public List<String> extractLocationEntities(String alt){
        String[] tokens = tokenizer.tokenize(alt);
        Span[] spans = locationNameFinder.find(tokens);
        List<String> entities = new ArrayList<>();

        for(Span span:spans){
            StringBuilder sb = new StringBuilder();
            for(int i=span.getStart(); i< span.getEnd(); i++){
                sb.append(tokens[i]).append(" ");
            }
            entities.add(sb.toString().trim());
        }
        locationNameFinder.clearAdaptiveData();
        return entities;
    }

    public List<String> extractAllEntities(String alt){
        List<String> persons  = extractPersonEntities(alt);
        List<String> organizations = extractOrganizationEntities(alt);
        List<String> locations = extractLocationEntities(alt);

        ArrayList<String> combinedList = new ArrayList<>(persons);
        combinedList.addAll(organizations);
        combinedList.addAll(locations);


        return combinedList;
    }

}
