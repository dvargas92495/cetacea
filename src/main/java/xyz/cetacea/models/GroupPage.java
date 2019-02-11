package xyz.cetacea.models;

import java.util.List;

public class GroupPage {
    private List<GroupView> groups;
    public GroupPage(List<GroupView> groups) {
        this.groups = groups;
    }
    public List<GroupView> getGroups() { return groups; }
}
