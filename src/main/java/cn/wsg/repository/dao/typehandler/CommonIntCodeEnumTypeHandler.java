package cn.wsg.repository.dao.typehandler;

import cn.wsg.commons.lang.function.IntCodeSupplier;
import cn.wsg.commons.web.mybatis.IntCodeEnumTypeHandler;
import cn.wsg.repository.common.enums.CelebrityStatus;
import cn.wsg.repository.common.enums.CollectStatus;
import cn.wsg.repository.common.enums.Gender;
import cn.wsg.repository.common.enums.Genre;
import cn.wsg.repository.common.enums.ReadStatus;
import cn.wsg.repository.common.enums.WorkType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author Kingen
 * @see IntCodeEnumTypeHandler
 */
@MappedJdbcTypes(value = {JdbcType.TINYINT})
@MappedTypes(value = {
    CollectStatus.class, ReadStatus.class, WorkType.class, Gender.class, CelebrityStatus.class,
    Genre.class
})
public class CommonIntCodeEnumTypeHandler<E extends Enum<E> & IntCodeSupplier>
    extends IntCodeEnumTypeHandler<E> {

    public CommonIntCodeEnumTypeHandler(Class<E> type) {
        super(type);
    }
}
