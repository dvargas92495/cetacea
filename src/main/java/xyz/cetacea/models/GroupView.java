package xyz.cetacea.models;

import xyz.cetacea.data.tables.pojos.Groups;
import xyz.cetacea.data.tables.pojos.Users;

import java.util.List;

public class GroupView {
    private Groups group;
    private List<Users> members;
    private boolean isAdmin;
    public GroupView(Groups group, List<Users> members, boolean isAdmin) {
        this.group = group;
        this.members = members;
        this.isAdmin = isAdmin;
    }
    public Groups getGroup() { return group; }
    public List<Users> getMembers() { return members; }
    public boolean getIsAdmin() { return isAdmin; }
}
