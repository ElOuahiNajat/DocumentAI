package fr.norsys.documentai.documents.entitySpecs;

import fr.norsys.documentai.documents.entities.Document;
import org.springframework.data.jpa.domain.Specification;

public class DocumentFileTypeSpecification {

    public static Specification<Document> hasFileType(String type) {
        return (root, query, builder) ->
                builder.equal(builder.lower(root.get("fileType")), type.toLowerCase());
    }
}
