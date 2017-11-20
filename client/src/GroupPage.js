import React from 'react'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import Rectangle from './components/Rectangle.js'

import styled from 'styled-components'
import { Menu, MenuItem, MenuDivider, Tab2, Tabs2, Card } from '@blueprintjs/core'


const GroupMenu = styled(Menu)`
  width: 15%;
  height: 100vh;
  display: inline-block;
  border-radius: 0px;
  border-color: #424242;
  border-style: solid;
  border-width: 0px 1px 1px 1px;
  background: #616161;
  .pt-menu-item {
    line-height: 65px;
  }
  margin: 0px 20px 0px 0px`
const GroupMenuItem = styled(MenuItem)`
  color: #FAFAFA;
  font-family: Allerta;
  font-size: 17px;
  font-weight: thin;
  text-align: center;`
const GroupTabs = styled(Tabs2)`
  display: block;
  margin-top: 10px;
  margin-right: 0px;
  font-family: Allerta;
  .pt-tab {
    font-size: 12px;
    padding-right: 20px;
    padding-left: 20px;
  }
  .pt-tab[aria-selected="true"]{
    border-radius: 6px 6px 0px 0px;
    background: #FFFFFF;
  }
  .pt-tab-list > *:not(:last-child) {
    margin-right: 0px;
  }
  .pt-tab-panel {
    margin-top: 0px;
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
`
const MemberEmail = styled.div`
  width: 30%;
  display: inline-block;
`

class GroupPage extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      pages: [ ['Help', '/'], ['Settings', '/' ], ['Groups', '/group'], ['Journal', '/journal']],
      id: 6,
      groups: [],
      currentGroupId: null,
      currentGroupMembers: null
    }

    var self = this
    fetch('/api/group?user_id='+this.state.id).then(function(resp){
      return resp.json();
    }).then(function(body){
      var i
      var arr = []
      for (i = 0; i < body.length; i++){
        console.log(body[i].name.trim())
        arr.push(body[i])
      }
      self.setState({groups: arr})
    })

    this.handleClick = this.handleClick.bind(this)
  }

  componentDidMount () {
    console.log(this.state.groups)
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
      console.log(self.state.currentGroupMembers)
    })
  }

  pickGroup () {
    if (!this.state.currentGroupView) {
      return false
    }
    else {
      return true
    }
  }

  renderGroupMembers () {
    console.log(this.state.currentGroupMembers)
    if (this.state.currentGroupMembers != null) {
      return (
        <GroupCard>
          {this.state.currentGroupMembers.map(member => <MemberCard key={member.id}><MemberName key={member.id+"n"}>{member.firstName + " " + member.lastName}</MemberName><MemberEmail key={member.id+"e"}>{member.email}</MemberEmail></MemberCard>)}
        </GroupCard>
      )
    }

    // {this.state.groups.map(group => <div key={group.id+'d'}><GroupMenuItem key={group.id} text={group.name.trim()} onClick={this.handleClick.bind(this, group.id)}/><MenuDivider key={group.id+'l'} /></div>)}
    // <MemberCard>
    //   <MemberName>
    //     David Vargas
    //   </MemberName>
    //   <MemberEmail>
    //     {"dvargas@mit.edu"}
    //   </MemberEmail>
    // </MemberCard>
  }

  renderGroupSettings () {
    return (
      <GroupCard>
        <MemberCard>
          settings
        </MemberCard>
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


  render () {
    console.log(this.state.groups)
    return (
      <div>
        <NavBar pages={this.state.pages} />
        <GroupMenu>
          <GroupMenuItem text="New Group" />
          <MenuDivider />
          {this.state.groups.map(group => <div key={group.id+'d'}><GroupMenuItem key={group.id} text={group.name.trim()} onClick={this.handleClick.bind(this, group.id)}/><MenuDivider key={group.id+'l'} /></div>)}
        </GroupMenu>
        {this.renderGroupContent(this.state.currentGroupId)}
      </div>
    )
  }
}

export default GroupPage
