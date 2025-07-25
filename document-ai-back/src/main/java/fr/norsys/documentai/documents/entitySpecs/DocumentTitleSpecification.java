package fr.norsys.documentai.documents.entitySpecs;

import fr.norsys.documentai.documents.entities.Document;
import org.springframework.data.jpa.domain.Specification;


public class DocumentTitleSpecification {

    public static Specification<Document> containsTitle(String title) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }
}
