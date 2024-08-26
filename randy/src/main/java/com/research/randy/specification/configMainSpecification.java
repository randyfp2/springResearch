package com.research.randy.specification;

import com.research.randy.model.configMain;
import org.springframework.data.jpa.domain.Specification;

public class configMainSpecification {
    public static Specification<configMain> hasKeyGroup(String keygroup) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("keygroup"), keygroup);
    }

    public static Specification<configMain> isEnabled() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isenable"));
    }
}
