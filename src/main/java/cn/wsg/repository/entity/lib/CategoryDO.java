package cn.wsg.repository.entity.lib;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Kingen
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDO implements Serializable {

    public static final String ROOT = "root";

    private static final long serialVersionUID = 1L;

    private String idx;

    private String title;

    private String superIdx;

    private Boolean leaf;
}