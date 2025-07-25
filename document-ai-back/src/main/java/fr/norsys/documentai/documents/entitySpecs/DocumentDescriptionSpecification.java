package fr.norsys.documentai.documents.entitySpecs;

import fr.norsys.documentai.documents.entities.Document;
import org.springframework.data.jpa.domain.Specification;

public class DocumentDescriptionSpecification {

    public static Specification<Document> containsDescription(String description) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }
}
