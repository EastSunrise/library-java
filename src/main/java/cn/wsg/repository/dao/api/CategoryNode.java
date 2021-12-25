package cn.wsg.repository.dao.api;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The category as a tree node.
 *
 * @author Kingen
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryNode {

    private String idx;

    private String title;

    private List<CategoryNode> children;
}
