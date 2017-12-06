import React from 'react'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'

import styled from 'styled-components'
import { Menu, MenuItem, MenuDivider, Tab2, Tabs2, Card, Icon } from '@blueprintjs/core'
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
const NewGroupTitle = styled.span`
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

class GroupPage extends React.Component {
  constructor(props) {
    super(props)
    const userId = props.location.state.userId;
    this.state = {
      userId: userId,
      groups: [],
      currentGroupId: null,
      currentGroupMembers: null
    }

    var self = this
    fetch('/api/group?user_id='+this.state.userId).then(function(resp){
      return resp.json();
    }).then(function(body){
      var i
      var arr = []
      for (i = 0; i < body.length; i++){
        arr.push(body[i])
      }
      self.setState({groups: arr})
      self.setState({currentGroupId: arr[0].id})
      self.setState({currentGroupMembers: self.getGroupMembers(arr[0].id)})
    })
    this.handleClick = this.handleClick.bind(this)
  }

  handleClick (id, e) {
    this.getGroupMembers(id)
  }

  getGroupMembers(groupId){
    var self = this
    fetch('/api/usergroup?group_id='+groupId).then(function(resp){
      return resp.json();
    }).then(function(body){
      self.setState({currentGroupMembers: body})
    })
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


  render () {
    return (
      <div>
        <NavBar/>
        <GroupMenu>
          <GroupMenuItem text={<div><Icon iconName="plus" iconSize={20} style={{fontSize: "30px"}}/><NewGroupTitle>New Group</NewGroupTitle></div>} />
          <MenuDivider />
          {this.state.groups.map(group => (
            <div key={group.id+'d'}>
              <GroupMenuItem key={group.id} text={this.renderGroupMenuItem(group)} onClick={this.handleClick.bind(this, group.id)}/>
              <MenuDivider key={group.id+'l'} />
            </div>
          ))}
        </GroupMenu>
        {this.renderGroupContent(this.state.currentGroupId)}
      </div>
    )
  }
}

// class SettingsRow extends React.Component {
//   constructor()
// }

export default GroupPage
