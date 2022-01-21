package cn.wsg.repository.com.clcindex;

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
