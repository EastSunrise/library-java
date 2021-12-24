package cn.wsg.library.dao.typehandler;

import cn.wsg.commons.lang.function.IntCodeSupplier;
import cn.wsg.commons.web.mybatis.IntCodeEnumTypeHandler;
import cn.wsg.library.common.enums.CollectStatus;
import cn.wsg.library.common.enums.ReadStatus;
import cn.wsg.library.common.enums.WorkType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author Kingen
 * @see IntCodeEnumTypeHandler
 */
@MappedJdbcTypes(value = {JdbcType.TINYINT})
@MappedTypes(value = {CollectStatus.class, ReadStatus.class, WorkType.class})
public class CommonIntCodeEnumTypeHandler<E extends Enum<E> & IntCodeSupplier> extends IntCodeEnumTypeHandler<E> {

    public CommonIntCodeEnumTypeHandler(Class<E> type) {
        super(type);
    }
}
