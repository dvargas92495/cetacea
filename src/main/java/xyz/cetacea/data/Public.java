/*
 * This file is generated by jOOQ.
*/
package xyz.cetacea.data;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import xyz.cetacea.data.tables.Groups;
import xyz.cetacea.data.tables.Journals;
import xyz.cetacea.data.tables.UserGroupLinks;
import xyz.cetacea.data.tables.Users;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1484975496;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.groups</code>.
     */
    public final Groups GROUPS = xyz.cetacea.data.tables.Groups.GROUPS;

    /**
     * The table <code>public.journals</code>.
     */
    public final Journals JOURNALS = xyz.cetacea.data.tables.Journals.JOURNALS;

    /**
     * The table <code>public.user_group_links</code>.
     */
    public final UserGroupLinks USER_GROUP_LINKS = xyz.cetacea.data.tables.UserGroupLinks.USER_GROUP_LINKS;

    /**
     * The table <code>public.users</code>.
     */
    public final Users USERS = xyz.cetacea.data.tables.Users.USERS;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        List result = new ArrayList();
        result.addAll(getSequences0());
        return result;
    }

    private final List<Sequence<?>> getSequences0() {
        return Arrays.<Sequence<?>>asList(
            Sequences.GROUPS_ID_SEQ,
            Sequences.JOURNALS_ID_SEQ,
            Sequences.USER_GROUP_LINKS_ID_SEQ,
            Sequences.USERS_ID_SEQ);
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Groups.GROUPS,
            Journals.JOURNALS,
            UserGroupLinks.USER_GROUP_LINKS,
            Users.USERS);
    }
}
