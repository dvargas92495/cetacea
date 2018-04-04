import React from 'react'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import {Dialog} from '@blueprintjs/core'
import moment from 'moment';

import styled from 'styled-components'
import { Menu, MenuItem, MenuDivider, Tab2, Tabs2, Card, Icon, Tooltip, Position } from '@blueprintjs/core'
import {TimePicker} from '@blueprintjs/datetime'

const GroupMenu = styled(Menu)`
  width: 15%;
  height: 100vh;
  display: inline-block;
  border-radius: 0px;
  border-color: #424242;
  border-style: solid;
  border-width: 0px 1px 1px 1px;
  background: #616161;
  margin: 0px 20px 0px 0px;`
const GroupMenuItem = styled(MenuItem)`
  color: #FAFAFA;
  text-align: center;
  outline: 0px;
  padding: 0px;
  &:focus {
    color: #106ba3;
    background: white;
  }
  `
const GroupTabs = styled(Tabs2)`
  display: block;
  margin-top: 10px;
  margin-right: 0px;
  font-family: Allerta;
  .pt-tab {
    font-size: 12px;
    padding-right: 8%;
    padding-left: 8%;
    border-radius: 6px 6px 0px 0px;
    outline: 0px;
  }
  .pt-tab[aria-selected="true"]{
    border-radius: 6px 6px 0px 0px;
    background: #FFFFFF;
    font-color: #000000;
  }
  .pt-tab-list > *:not(:last-child) {
    margin-right: 0px;
  }
  .pt-tab-panel {
    margin-top: 0px;
  }
  .pt-tab[aria-selected="false"]{
    background: #E0E0E0;
    font-color: #424242;

  }
  `
const GroupCard = styled(Card)`
  display: block;
  height: 80vh;
  border-radius: 0px 3px 3px 3px;`
const GroupContent = styled.div`
  display: inline-block;
  vertical-align: top;
  width: 80%;`
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
  margin-top: 5px;`
const GroupDescription = styled.span`
  display:block;
  font-size: 10px;
  font-style: italic;
  margin-top: 10px;
  margin-bottom: 7px;
  font-family: 'Open Sans', sans-serif;`
const TooltipFix = styled(Tooltip)`
  display: block;
`

const GridContainer = styled.form`
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


class GroupPage extends React.Component {
  constructor(props) {
    super(props)
    const userId = props.location.state ? props.location.state.userId : 0
    this.state = {
      userId: userId,
      groups: [],
      currentGroupId: null,
      currentGroupMembers: null,
      newGroupIsOpen: false,
      newGroupName: '',
      newGroupDescription: '',
      newGroupMembers: ''
    }

    this.getGroups({userId: userId})
    this.handleClick = this.handleClick.bind(this)
    this.handleNewGroupClick = this.handleNewGroupClick.bind(this)

    this.handleNewNameChange = this.handleNewNameChange.bind(this)
    this.handleNewDescriptionChange = this.handleNewDescriptionChange.bind(this)
    this.handleNewMembersChange = this.handleNewMembersChange.bind(this)

    this.toggleNewGroupDialog = this.toggleNewGroupDialog.bind(this)
    this.handleNewGroupSubmit = this.handleNewGroupSubmit.bind(this)

  }

  handleClick (id, e) {
    this.getGroupMembers(id)
  }

  handleNewGroupClick(e) {
    this.toggleNewGroupDialog()
  }

  getGroups(userObj){
    var self = this;
    if (userObj.userId == 0) return
    fetch('/api/group?user_id='+userObj.userId).then(function(resp){
      return resp.json();
    }).then(function(body){
      var i
      var arr = []
      for (i = 0; i < body.length; i++){
        arr.push(body[i])
      }
      self.setState({groups: arr, userId: userObj.userId})
      if (arr.length > 0) {
        self.setState({currentGroupId: arr[0].id})
        self.setState({currentGroupMembers: self.getGroupMembers(arr[0].id)})
      }
    })
  }

  getGroupMembers(groupId){
    var self = this
    fetch('/api/usergroup?group_id='+groupId).then(function(resp){
      return resp.json();
    }).then(function(body){
      self.setState({currentGroupMembers: body})
      self.setState({currentGroupId: groupId})
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
    }).then(function(){
      self.toggleNewGroupDialog()
    });
  }

  renderNewGroupForm() {
    return (
      <div>
        <Dialog isOpen={this.state.newGroupIsOpen} onClose={this.toggleNewGroupDialog} title='Create New Group'>
          <GridContainer>
            <NewGroupLabel>{"Group Name:"}<input className="pt-input" type="text" value={this.state.newGroupName} onChange={this.handleNewNameChange}/></NewGroupLabel>
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
            <SaveButton text="Save" press={this.handleNewGroupSubmit}></SaveButton>
          </GridContainer>
        </Dialog>
      </div>
    )
  }

  isUserAdmin(user_id, group_id){
    fetch('/api/usergroup?group_id='+group_id+'&user_id='+user_id).then(function(resp){
      return resp.json();
    }).then(function(body){
      console.log(body);
    })
  }

  memberButton(id) {
    this.isUserAdmin(id, this.state.currentGroupId)

    // var self = this
    // fetch('/api/usergroup?group_id='+groupId).then(function(resp){
    //   return resp.json();
    // }).then(function(body){
    //   self.setState({currentGroupMembers: body})
    //   self.setState({currentGroupId: groupId})
    // })
    //
    // var self = this
    // var isAdmin = false
    //
    // fetch('/api/usergroup?group_id='+this.state.currentGroupId+'&user_id='+id).then(function(resp){
    //   isAdmin =
    // })
    //
    //
    // if (isAdmin && id != this.state.userId){
    //   return(
    //     <Button text="Remove" />
    //   )
    // }
    //
    // else if (!isAdmin && id == this.state.userId) {
    //   return(
    //     <Button text="Leave Group" />
    //   )
    // }
    //
    //
    // console.log(this.state.currentGroupMembers)
    // console.log(this.state.currentGroupId)

    //
    // if (id == this.state.userId) {
    //   return (
    //     <Button text="Leave Group" />
    //   )
    // }
    //
    // else if (true) {
    //
    // }
    //
    // return (
    //   <Button text={id}/>
    // )
  }

  renderGroupMembers () {
    if (this.state.currentGroupMembers != null) {
      return (
        <GroupCard>
          {this.state.currentGroupMembers.map(member => (
            <MemberCard key={member.id}>
              <Icon iconName="pt-icon-user" iconSize={20} />
              <MemberName key={member.id+"n"}>{member.firstName + " " + member.lastName}</MemberName>
              <MemberEmail key={member.id+"e"}>{member.email}</MemberEmail>
              {this.memberButton(member.id)}
            </MemberCard>
          ))}
        </GroupCard>
      )
    }
  }

  renderGroupSettings () {
    return (
      <GroupCard>
        {"Group Name: "}
        <input className="pt-input" type="text" placeholder="get group name" />
        {"Group Description: "}
        <textarea className="pt-input" placeholder="get group description " />
        {"Group Membership Model: "}
        <div className="pt-select">
          <select>
            <option defaultValue>Choose a membership model...</option>
            <option value="1">All Members Approve</option>
            <option value="2">Group Owner Approves</option>
            <option value="3">Any Member Can Approve</option>
          </select>
        </div>
        {"Add Member"}
        <input className="pt-input" type="text" />
        <hr/>
        {"Email Frequency: "}
        <div className="pt-select">
          <select>
            <option defaultValue>Choose...</option>
            <option value="1">Daily</option>
          </select>
        </div>
        {"Email Send Time: "}
        <TimePicker showArrowButtons={true}/>

      </GroupCard>
    )
  }

  renderGroupContent(groupId){
    return (
      <GroupContent>
        <GroupTabs id="group">
          <Tab2 id="members" title="Members" panel={this.renderGroupMembers()}/>
          <Tab2 id="settings" title="Settings" panel={this.renderGroupSettings()}/>
        </GroupTabs>
      </GroupContent>
    )
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


  render () {
    return  (
      <div>
        <NavBar redirect={this.redirectToHome.bind(this)} onlogin={this.getGroups.bind(this)} userId={this.state.userId}/>
        <GroupMenu>
          <GroupMenuItem text={<div><Icon iconName="plus" iconSize={20} style={{fontSize: "30px"}} onClick={this.handleNewGroupClick.bind(this)} /><NewGroupTitle>New Group</NewGroupTitle></div>} />
          <MenuDivider />
          {this.state.groups.map(group => (
            <div key={group.id+'d'}>
              <GroupMenuItem key={group.id} text={this.renderGroupMenuItem(group)} onClick={this.handleClick.bind(this, group.id)}/>
              <MenuDivider key={group.id+'l'} />
            </div>
          ))}
        </GroupMenu>
        {this.renderGroupContent(this.state.currentGroupId)}
        {this.renderNewGroupForm()}
      </div>
    )
  }
}

// class SettingsRow extends React.Component {
//   constructor()
// }

export default GroupPage
