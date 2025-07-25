package fr.norsys.documentai.documents.entitySpecs;

import fr.norsys.documentai.documents.entities.Document;
import org.springframework.data.jpa.domain.Specification;

public class DocumentAuthorSpecification {

    public static Specification<Document> containsAuthor(String author) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }
}
