package com.lazer.lab3.component;

import com.lazer.lab3.request.SearchRequest;
import jakarta.annotation.PreDestroy;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.lucene.document.Field.Store.YES;

@Component
public class Search {
    private static final QueryParser QUERY_PARSER = new QueryParser("white_id", new StandardAnalyzer());

    private String indexPath = "./src/main/java/com/lazer/lab3/runtime_files";

    private Directory indexDirectory;

    public Search(String indexPath, int rowNumber) {
        this.indexPath = indexPath;
        try {
            index(rowNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Search(int rowNumber) {
        try {
            index(rowNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Search() {
        try {
            index();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Document> search(SearchRequest inQuery) {
        try (var indexReader = DirectoryReader.open(indexDirectory)) {
            var indexSearcher = new IndexSearcher(indexReader);
            var query = QUERY_PARSER.parse(inQuery.getSearchString());
            var result = indexSearcher.search(query, 10);
            var storedFields = indexSearcher.storedFields();
            List<Document> documents = new ArrayList<>();
            for (var score : result.scoreDocs) {
                var document = storedFields.document(score.doc);
                documents.add(document);
            }
            return documents;
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void index() throws IOException {
        index(10_000);
    }

    private void index(int rowNumber) throws IOException {
        index("./src/main/java/com/lazer/resources/games.csv", rowNumber);
    }

    private void index(String dataFileName, int rowNumber) throws IOException {
        indexDirectory = FSDirectory.open(new File(indexPath).toPath());
        if (indexDirectory.listAll().length > 0) {
            return;
        }

        try (var indexWriter = new IndexWriter(indexDirectory, new IndexWriterConfig())) {
            boolean isFirstLine = true;
            var size = 0;
            var buffer = new ArrayList<Document>();
            try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    var document = mapToDocument(line);
                    if (document == null) {
                        continue;
                    }
                    if (buffer.size() < 1024) {
                        buffer.add(document);
                    } else {
                        indexWriter.addDocuments(buffer);
                        buffer.clear();
                    }
                    size++;
                    if (size == rowNumber) {
                        break;
                    }
                }
            }
            if (!buffer.isEmpty()) {
                indexWriter.addDocuments(buffer);
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private Document mapToDocument(String row) {
        String[] columns = row.split(",");
        if (columns.length <= 1) {
            return null;
        }

        var document = new Document();
        document.add(new TextField("id", columns[0], YES));
        document.add(new TextField("rated", columns[1], YES));
        document.add(new TextField("created_at", columns[2], YES));
        document.add(new TextField("last_move_at", columns[3], YES));
        document.add(new FloatField("turns", Float.parseFloat(columns[4]), YES));
        document.add(new TextField("victory_status", columns[5], YES));
        document.add(new TextField("winner", columns[6], YES));
        document.add(new TextField("increment_code", columns[7], YES));
        document.add(new TextField("white_id", columns[8], YES));
        document.add(new TextField("white_rating", columns[8], YES));
        return document;
    }

    @PreDestroy
    private void preDestroy() throws IOException {
        indexDirectory.close();
    }

}
