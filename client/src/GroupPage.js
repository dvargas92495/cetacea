import React from 'react'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import {Dialog, Popover} from '@blueprintjs/core'
import moment from 'moment';

import styled from 'styled-components'
import { Menu, MenuItem, MenuDivider, Tab, Tabs, Card, Icon, Tooltip, Position } from '@blueprintjs/core'
import {TimePicker} from '@blueprintjs/datetime'
import { DatePicker } from "@blueprintjs/datetime";

const GroupTabs = styled(Tabs)`
  display: block;
  margin-top: 10px;
  margin-right: 0px;
  font-family: Allerta;
  bp3-tab {
    font-size: 12px;
    padding-right: 80px;
    padding-left: 80px;
    border-radius: 6px 6px 0px 0px;
    outline: 0px;
  }
  bp3-tab[aria-selected="true"]{
    border-radius: 6px 6px 0px 0px;
    background: #FFFFFF;
  }
  bp3-tab-list > *:not(:last-child) {
    margin-right: 0px;
  }
  bp3-tab-panel {
    margin-top: 0px;
  }
  bp3-tab[aria-selected="false"]{
    background: #E0E0E0;
    font-color: #424242;
  }

  `
const GroupCard = styled(Card)`
  display: block;
  border-radius: 0px 3px 3px 3px;
  `
const GroupContent = styled.div`
  display: inline-block;
  vertical-align: top;
  width: 100%;
  padding: 0px 15px 25px 15px;`
const MemberCard = styled(Card)`
  border: gray;`
const MemberName = styled.div`
  width: 30%;
  display: inline-block;
  margin-left: 35px;`
const MemberEmail = styled.div`
  width: 30%;
  display: inline-block;`
const NewGroupTitle = styled.div`
  display: block;
  font-family: Allerta;
  font-size: 11px;`
const GroupTitle = styled.span`
  display: block;
  font-size: 17px;
  font-family: Allerta;
  margin-top: 3px;
`
const GroupDescription = styled.span`
  display:block;
  font-size: 10px;
  font-style: italic;
  font-family: 'Open Sans', sans-serif;
  margin-bottom: 3px;
  `
const TooltipFix = styled(Tooltip)`
  display: block;
`

const GridContainer = styled.div`
  padding: 15px 30px 10px 30px;
  font-family: Allerta;
`
const NewGroupLabel = styled.label`
  display: grid;
  grid-template-columns: 35% 65%;
  margin-bottom: 10px;
`
const CaptionText = styled.div`
  font-size: 9px;
  padding-left: 5px;
  padding-right: 5px;
  padding-top: 2px;
`

const SaveButton = styled(Button)`
  float: right;
`
const PageContainer = styled.div`
  display: grid;
  grid-template-columns: 15% auto;
  grid-template-rows: auto 1fr;
  height: 100vh;
`
const NavBarGrid = styled.div`
  grid-column: 1 / 3;
`

const RemoveButtons = styled.button`
  width: 100px;
  height: 40px;
  color: #757575;
  font-family: 'Allerta';
  font-size: 12px;
  background: #FFFFFF;
  border: 1px solid #757575;
  border-radius: 10px;
  display: inline-block;
  &:hover {
    cursor: pointer;
    cursor: hand;
  }`

const SavePopover = styled(Popover)`
  bp3-popover {
    width: 320px;
    padding: 10px;
    padding-right: 3px;
    background-color: white;
    color: indianred;
  }
`
const TextWrapper = styled.div`
  text-align: center;
`
const ButtonWrapper = styled.div`
  display: flex;
  margin-top: 20px;
  justify-content: space-around;

`
const ButtonRed = styled.button`
  color: indianred;
  width: 100px;
  height: 40px;
  font-family: 'Allerta';
  font-size: 12px;
  background: #FFFFFF;
  border: 1px solid #757575;
  border-radius: 10px;
  display: inline-block;
  &:hover {
    cursor: pointer;
    cursor: hand;
  }
`
const Vertical = styled.div`
  border-left: 2px solid #424242;
  margin-left: 10px;
`
const CalendarWrapper = styled.div`
  display: grid;
  grid-template-columns: 1fr 20px 4fr;
`
const JournalDisplay = styled.div`
  padding-left: 10px;
`
const Journal = styled.div`
  white-space: pre-wrap;
  font-family: 'Open Sans';
`
const DateHeader = styled.h1`
  font-size: 25px;
  text-align: right;
`
const NoJournal = styled.div`
  text-align: center;
  padding-top: 75px;
`
const GroupSelect = styled.div`
  display: inline-block;
  border-color: #424242;
  border-style: solid;
  border-width: 0px 1px 1px 1px;
  background: #616161;
  padding: 5px;
`
const NewGroupButton = styled.button`
  width: 100%;
  border: none;
  background: none;
  border-radius: 2px;
  color: white;
  padding-bottom: 10px;
  &:hover{
    background: #6f7377;
    cursor: pointer;
  }
  &:focus{
    outline: 0px;
  }

`
const UserGroupTabs = styled(Tabs)`
  text-align: center;
  color: white;
  bp3-tab-list {
    width: 100%;
  }
  bp3-tab[aria-selected="true"]{
    background: #FFFFFF;
    box-shadow: none;
  }
  bp3-tab[aria-selected="false"]{
    color: white;
    &:hover {
      background: #6f7377;
    }
  }
  bp3-tab {
    border-radius: 2px;
    border-bottom: rgba(16, 22, 26, 0.15) solid thin;
    &:focus {
      outline: 0px;
    }
  }
`
const Horizontal = styled.hr`
  margin: 0px;
  margin-bottom: 5px;
`


class GroupPage extends React.Component {
  constructor(props) {
    super(props)
    const userId = props.location.state ? props.location.state.userId : 0
    this.state = {
      userId: userId,
      groups: [],
      currentGroupId: null,
      newGroupIsOpen: false,
      newGroupName: '',
      newGroupDescription: '',
      newGroupMembers: '',
      addMember: '',
      savePopover: false,
      currentTabId: "members",
      deleteWarningIsOpen: false,
      maxDate: moment().subtract(1, "day").toDate(),
      minDate: moment("2017-11-01").toDate(),
      selectedDate: moment().subtract(1, "day").format("dddd, MMMM D, YYYY").toString(),
      currentJournal: "There are no journals for your selected day. Please select a new date.",
      currentUserGroupId: null,
      test: true

    }

    this.getGroups({userId: userId})
    this.handleClick = this.handleClick.bind(this)
    this.handleNewGroupClick = this.handleNewGroupClick.bind(this)

    this.handleNewNameChange = this.handleNewNameChange.bind(this)
    this.handleNewDescriptionChange = this.handleNewDescriptionChange.bind(this)
    this.handleNewMembersChange = this.handleNewMembersChange.bind(this)

    this.toggleNewGroupDialog = this.toggleNewGroupDialog.bind(this)
    this.handleNewGroupSubmit = this.handleNewGroupSubmit.bind(this)

    this.handleAddMemberChange = this.handleAddMemberChange.bind(this)
    this.handleAddMemberSubmit = this.handleAddMemberSubmit.bind(this)

    this.toggleDeleteWarningDialog = this.toggleDeleteWarningDialog.bind(this)
  }

  handleClick (id, e) {
    this.setState({currentGroupId: id})
    this.handleTabChange("members")
  }

  getGroups(userObj){
    var self = this;
    if (userObj.userId == 0) return
    fetch('/api/group?user_id='+userObj.userId).then(function(resp){
      return resp.json();
    }).then(function(body){
      self.setState({groups: body.groups, userId: userObj.userId})

      if (body.groups.length > 0){
        self.setState({currentGroupId: body.groups[0].group.id})
        self.setState({currentUserGroupId: body.groups[0].group.id})
      }
    })
  }

  toggleNewGroupDialog() {
    this.setState({newGroupIsOpen: !this.state.newGroupIsOpen})
  }

  handleNewNameChange(e) {
    this.setState({newGroupName: e.target.value})
  }

  handleNewDescriptionChange(e) {
    this.setState({newGroupDescription: e.target.value})
  }

  handleNewMembersChange(e){
    this.setState({newGroupMembers: e.target.value})
  }

  handleNewGroupSubmit(e) {
    var sanitizeName = this.state.newGroupName.trim()
    var sanitizeBlurb = this.state.newGroupDescription.trim()

    if (sanitizeName != "" && sanitizeBlurb != ""){
      var self = this;
      var timestamp = moment().toDate();
      fetch('/api/group', {
        method: 'POST',
        body: JSON.stringify({
          name: this.state.newGroupName,
          description: this.state.newGroupDescription,
          timestamp_created: timestamp,
          created_by: this.state.userId,
          members: this.state.newGroupMembers
        })
      }).then(function(resp){
        return resp.json();
      }).then(function(body){
        self.toggleNewGroupDialog()
        var newGroups = self.state.groups
        var groupKey = Object.keys(body)[0]
        newGroups[groupKey] = body[groupKey]
        self.setState({groups: newGroups})
        self.setState({
          newGroupName: "",
          newGroupDescription: "",
          newGroupMembers: ""
        })

      });
    }
    else {
      this.setState({savePopover: true})
    }
  }

  popoverInteraction(popoverChange){
    this.setState({savePopover: popoverChange})
  }

  renderNewGroupForm() {
    return (
      <div>
        <Dialog isOpen={this.state.newGroupIsOpen} onClose={this.toggleNewGroupDialog} title='Create New Group'>
        <GridContainer>
          <NewGroupLabel>{"Group Name:"}<input required className="pt-input" type="text" value={this.state.newGroupName} onChange={this.handleNewNameChange}/></NewGroupLabel>
          <NewGroupLabel>{"Group Blurb:"}<textarea className="pt-input" value={this.state.newGroupDescription} onChange={this.handleNewDescriptionChange}/></NewGroupLabel>
          <NewGroupLabel>
            <div>
              {"Add Members:"}
              <CaptionText>
              <i>
                {"Enter emails separated "}
                <br/>
                {"by commas."}
              </i>
              </CaptionText>
            </div>
            <textarea className="pt-input" rows="4" value={this.state.newGroupMembers} onChange={this.handleNewMembersChange}/>
          </NewGroupLabel>
          <div style={{float: "right"}}>
            <SavePopover content={"You must fill out the Group Name and Group Blurb fields before you can submit!"} isOpen={this.state.savePopover} inline={true} position={Position.LEFT}  onInteraction={this.popoverInteraction.bind(this)}>
              <SaveButton press={this.handleNewGroupSubmit} text={"Submit"}></SaveButton>
            </SavePopover>
          </div>
        </GridContainer>
        </Dialog>
      </div>
    )
  }

  handleRemove(userId, e){
    var self = this;
    if (userId == this.state.userId){
      var purpose = "leave"
    }
    else {
      var purpose = "remove"
    }
    fetch('/api/usergroup', {
      method: 'DELETE',
      body: JSON.stringify({
        purpose: purpose,
        user_id: userId,
        group_id: this.state.currentGroupId
      })
    }).then(function(resp){
      return resp.json();
    }).then(function(body){
      if (userId == self.state.userId){
        //leave
        var newGroups = self.state.groups
        delete newGroups[self.state.currentGroupId]
        var newKey = Object.keys(newGroups)[0]
        self.setState({currentGroupId: newKey})
        self.handleUserGroupTabChange(self.state.currentGroupId)
      }
      else{
        //remove
        self.state.groups[self.state.currentGroupId].members = body
        self.setState({test: !self.state.test})
      }
    })
  }

  isUser(userId, isAdmin){
    if (isAdmin){
      if (userId != this.state.userId){
        return (
          <RemoveButtons onClick={this.handleRemove.bind(this, userId)}>{"Remove"}</RemoveButtons>
        )
      }
    }
    else if (!isAdmin && userId == this.state.userId) {
      return (
        <RemoveButtons onClick={this.handleRemove.bind(this, userId)}>{"Leave Group"}</RemoveButtons>
      )
    }
  }

  renderGroupMembers () {
    const groupBundle = this.getCurrentGroup();
    if (this.state.currentGroupId != null && groupBundle != null) {
      if (groupBundle.isAdmin){
        return (
          <GroupCard>
            {groupBundle.members.map(member => (
              <MemberCard key={member.id}>
                <Icon iconName="pt-icon-user" iconSize={20} />
                <MemberName key={member.id+"n"}>{member.firstName + " " + member.lastName}</MemberName>
                <MemberEmail key={member.id+"e"}>{member.email}</MemberEmail>
                {this.isUser(member.id, true)}
              </MemberCard>
            ))}
          </GroupCard>
        )
      }
      else {
        return (
          <GroupCard>
            {groupBundle.members.map(member => (
              <MemberCard key={member.id}>
                <Icon iconName="pt-icon-user" iconSize={20} />
                <MemberName key={member.id+"n"}>{member.firstName + " " + member.lastName}</MemberName>
                <MemberEmail key={member.id+"e"}>{member.email}</MemberEmail>
                {this.isUser(member.id, false)}
              </MemberCard>
            ))}
          </GroupCard>
        )
      }
    }
  }

  handleAddMemberChange(e){
    this.setState({addMember: e.target.value})
  }

  handleAddMemberSubmit(e){
    var self = this;
    var timestamp = moment().toDate();

    fetch('/api/usergroup', {
      method: 'POST',
      body: JSON.stringify({
        email: this.state.addMember,
        group_id: this.state.currentGroupId,
        timestamp_joined: timestamp
      })
    }).then(function(resp){
      return resp.json()
    }).then(function(body){
      self.state.groups[self.state.currentGroupId].members = body
      self.setState({addMember: ""})
      self.setState({currentTabId: "members"})
    })
  }

  toggleDeleteWarningDialog(){
    this.setState({deleteWarningIsOpen: !this.state.deleteWarningIsOpen})
  }

  handleDeleteGroup(e){
    var self = this;

    fetch('/api/group', {
      method: 'DELETE',
      body: JSON.stringify({
        id: this.state.currentGroupId
      })
    }).then(function(){
      var newGroups = self.state.groups
      delete newGroups[self.state.currentGroupId]
      var newKey = Object.keys(newGroups)[0]
      self.setState({currentGroupId: newKey})
      self.toggleDeleteWarningDialog()
      self.handleUserGroupTabChange(self.state.currentGroupId)
    })

  }

  getCurrentGroup() {
    return this.state.groups.find(bundle => bundle.group.id == this.state.currentGroupId);
  }

  renderGroupDeleteWarning() {
    return (
      <div>
        <Dialog isOpen={this.state.deleteWarningIsOpen}
                onClose={this.toggleDeleteWarningDialog}
                title={"Are you sure you want to delete this group?"}>
          <GridContainer>
            <TextWrapper>
              {"All deletions are permanent!"}
            </TextWrapper>
            <ButtonWrapper>
              <ButtonRed onClick={this.handleDeleteGroup.bind(this)}>Delete</ButtonRed>
              <Button press={this.toggleDeleteWarningDialog} text={"Cancel"}/>
            </ButtonWrapper>
          </GridContainer>
        </Dialog>
      </div>
    )
  }

  renderGroupSettings () {
    const currentGroup = this.getCurrentGroup();
    return currentGroup != null && (
      <GroupCard>
        <GridContainer>
          <NewGroupLabel>{"Group Name:"}<input className="pt-input" type="text" placeholder={currentGroup.group.name.trim()}/></NewGroupLabel>
          <NewGroupLabel>{"Group Blurb:"}<textarea className="pt-input" placeholder={currentGroup.group.description} /></NewGroupLabel>
          <NewGroupLabel>{"Group Membership Model:"}
            <div className="pt-select pt-fill">
              <select>
                <option value="1">Basic Closed Model</option>
                <option disabled value="2">Basic Open Model</option>
                <option disabled value="3">Voting Model</option>
              </select>
            </div>
          </NewGroupLabel>
          <NewGroupLabel>
            <div>
              {"Add Members:"}
              <CaptionText>
                <i>
                  {"Enter emails separated "}
                  <br/>
                  {"by commas."}
                </i>
              </CaptionText>
            </div>
            <textarea className="pt-input" rows="4" value={this.state.addMember} onChange={this.handleAddMemberChange} />
          </NewGroupLabel>
          <NewGroupLabel><div></div>
            <div style={{textAlign: "right"}}>
              <Button press={this.handleAddMemberSubmit.bind(this)} text={"Update"}></Button>
            </div>
          </NewGroupLabel>
        </GridContainer>
        <hr/>
        <Button text={"Delete Group"} press={this.toggleDeleteWarningDialog}/>
        {this.renderGroupDeleteWarning()}
      </GroupCard>
    )
  }

  handleTabChange(e){
    this.setState({currentTabId: e})
    if (e == "past"){
      this.handleDateChange(this.state.maxDate)
    }
  }

  displayJournal(){
    return (
      <JournalDisplay>
        <DateHeader>
          {this.state.selectedDate}
        </DateHeader>
        <hr/>
        <Journal>
          {this.state.currentJournal}
        </Journal>
      </JournalDisplay>
    )
  }

  handleDateChange(e){
    var self = this;
    if (e == null) {
      return

    }
    var day = moment(e).format("YYYY-MM-DDTHH:mm:ssZ").toString()
    this.setState({selectedDate: moment(e).format("dddd, MMMM D, YYYY").toString()})

    fetch('/api/journal',{
       method: 'POST',
       body: JSON.stringify({
         id: this.state.userId,
         date: day,
       })
    }).then(function(resp){
      return resp.json();
    }).then(function(body){
      if (body.entry == null){
        self.setState({currentJournal: "There are no journals for your selected day. Please select a new date."})
      }
      else {
        self.setState({currentJournal: body.entry})
      }
    })

  }

  renderPastJournals(){
    return (
      <div>
        <GroupCard>
          <CalendarWrapper>
            <DatePicker onChange={this.handleDateChange.bind(this)} minDate={this.state.minDate} maxDate={this.state.maxDate} defaultValue={this.state.maxDate}/>
            <Vertical/>
            {this.displayJournal()}
          </CalendarWrapper>
        </GroupCard>
      </div>
    )
  }

  renderGroupContent(groupId){
    if (this.state.currentGroupId != null){
      const groupBundle = this.getCurrentGroup();
      if (groupBundle != null && groupBundle.isAdmin){
        return (
          <GroupContent>
            <GroupTabs id="group" selectedTabId={this.state.currentTabId} onChange={this.handleTabChange.bind(this)}>
              <Tab id="members" title="Members" panel={this.renderGroupMembers()}/>
              <Tab id="settings" title="Settings" panel={this.renderGroupSettings()}/>
              <Tab id="past" title="Past Journals" panel={this.renderPastJournals()}/>
            </GroupTabs>
          </GroupContent>
        )
      }
      else {
        return (
          <GroupContent>
            <GroupTabs id="group" selectedTabId={this.state.currentTabId} onChange={this.handleTabChange.bind(this)}>
              <Tab id="members" title="Members" panel={this.renderGroupMembers()}/>
              <Tab id="settings" disabled title="Settings" panel={this.renderGroupSettings()}/>
              <Tab id="past" title="Past Journals" panel={this.renderPastJournals()}/>
            </GroupTabs>
          </GroupContent>
        )
      }
    }
  }

  renderGroupMenuItem(group) {
    return (
      <div>
        <GroupTitle>{group.name.trim()}</GroupTitle>
        <GroupDescription>{group.description}</GroupDescription>
      </div>
    )
  }

  redirectToHome() { //TODO: I think this will go in the base page class
    this.props.history.push("/", {userId:0});
  }

  handleUserGroupTabChange(e){
    this.setState({currentGroupId: e})
    this.setState({currentUserGroupId: e})
    this.handleTabChange("members")
  }

  renderUserGroupTabs(){
    return (
      this.state.groups.map(groupBundle => groupBundle.group).map(group => (
        <Tab key={group.id} id={group.id} title={this.renderGroupMenuItem(group)} />
      ))
    )
  }

  render () {
    return  (
      <div>
        <PageContainer>
          <NavBarGrid>
            <NavBar redirect={this.redirectToHome.bind(this)} onlogin={this.getGroups.bind(this)} userId={this.state.userId}/>
          </NavBarGrid>
          <GroupSelect>
            <TooltipFix content={"This feature is not yet functional."} position={Position.RIGHT}>
              <NewGroupButton onClick={this.toggleNewGroupDialog.bind(this)}><Icon iconName="plus" iconSize={20} style={{fontSize: "30px"}}/><NewGroupTitle>{"New Group"}</NewGroupTitle></NewGroupButton>
            </TooltipFix>
            <Horizontal/>
            <UserGroupTabs id="usergroup" animate={false} vertical={true} onChange={this.handleUserGroupTabChange.bind(this)} selectedTabId={this.state.currentUserGroupId}>
              {this.renderUserGroupTabs()}
            </UserGroupTabs>
          </GroupSelect>
          {this.renderGroupContent(this.state.currentGroupId)}
        </PageContainer>
        {this.renderNewGroupForm()}
      </div>
    )
  }
}


export default GroupPage
