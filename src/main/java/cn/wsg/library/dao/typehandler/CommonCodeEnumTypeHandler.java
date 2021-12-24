package cn.wsg.library.dao.typehandler;

import cn.wsg.commons.lang.enums.Region;
import cn.wsg.commons.lang.function.CodeSupplier;
import cn.wsg.commons.web.mybatis.CodeEnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author Kingen
 * @see CodeEnumTypeHandler
 */
@MappedJdbcTypes(value = JdbcType.CHAR)
@MappedTypes(value = {Region.class})
public class CommonCodeEnumTypeHandler<E extends Enum<E> & CodeSupplier> extends CodeEnumTypeHandler<E> {

    public CommonCodeEnumTypeHandler(Class<E> type) {
        super(type);
    }
}
