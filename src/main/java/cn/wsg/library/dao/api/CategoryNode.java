package cn.wsg.library.dao.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
