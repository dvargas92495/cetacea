import React from 'react'
import styled from 'styled-components'
import {Link} from 'react-router-dom'
import Logo from '../../images/cetacea_logo.png'
import LoginPopup from './LoginPopup.js'

const Bar = styled.ul`
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #D4EDF0;
  height: 40px;
  font-family: Allerta;
  border: 1px solid #616161;
  border-radius: 0px;`
const List = styled.li`
  float: right;
  display: block;
  font-family: Allerta;
  color: #616161;
  text-align: center;
  padding: 10px 40px;
  text-decoration: none;
  &:hover {
    color: #3F51B5;
    text-decoration: none;
    cursor: pointer;
    cursor: hand;
  }`
const Page = styled(Link)`
  display: block;
  font-family: Allerta;
  color: #616161;
  text-align: center;
  text-decoration: none;
  &:hover {
    color: #3F51B5;
    text-decoration: none;
  }`
const LogoContainer = styled.img`
  height: 70%;
  margin-top: 4px;
  margin-left: 15px;`

class NavBar extends React.Component {
  constructor(props) {
    super(props)

    const loginPage = {text: 'Log In', method:this.openDialog.bind(this)}
    const logoutPage = {text: 'Log Out', method: this.logout.bind(this)}

    const aboutPage = {text: 'About', path:'/about'}
    const journalPage = {text: 'Journal', path: '/journal'}
    const groupPage = {text: 'Groups', path: '/group'}
    const settingsPage = {text: 'Settings', path: '/settings'}
    const helpPage = {text: 'Help', path:'/help'}

    this.loggedOutPages = [ aboutPage, loginPage]
    this.loggedInPages = [ logoutPage, helpPage, settingsPage, groupPage, journalPage]

    this.state = {
      userId: this.props.userId,
      pages: this.props.userId > 0 ? this.loggedInPages: this.loggedOutPages,
      isOpen: false
    }

    var self = this
    if (this.state.userId == 0) {
      this.isAuthenticated(function (a2) {
        self.login(a2.currentUser.get())
      })
    }
  }

  openDialog() {
    this.setState({isOpen: true })
  }

  closeDialog() {
    this.setState({isOpen: false })
  }

  login(user) {
    var id_token = user.getAuthResponse().id_token
    var self = this
    fetch('/api/login', {
      method: 'POST',
      body: JSON.stringify({
        idtoken: id_token,
        isSignup: "false"
      })
    }).then(function(resp){
      return resp.json();
    }).then(function(body){
      self.setState({userId: body.id,
                     pages: body.id > 0 ? self.loggedInPages: self.loggedOutPages,
                     isOpen: false})
      self.props.onlogin({userId: body.id});
    })
  }

  logout(){
    var self = this;
    self.isAuthenticated(function(a2){
      a2.signOut().then(function(){
        self.setState({userId: 0, pages: self.loggedOutPages})
        self.props.redirect()
      });
    });
  }

  isAuthenticated(callback){
    var self = this;
    gapi.load('auth2', function(){
      var a2 = gapi.auth2.init({
        client_id: '548992550759-kmikahq1pkfhffgps85151j5o2a6gduu.apps.googleusercontent.com',
        scope: 'https://www.googleapis.com/auth/plus.login'
      });
      a2.then(function(){
        if(a2.isSignedIn.get()) {
          callback(a2);
        } else {
          self.setState({userId: 0, pages: self.loggedOutPages})
          self.props.redirect()
        }
      });
    });
  }

  renderNavElements(listValue, i){
    if (listValue.path) {
      return (
        <List key={i}>
          <Page key={listValue.text} to={{pathname: listValue.path, state:{userId: this.state.userId}}}>{listValue.text}</Page>
        </List>
      )
    }
    else {
      return <List key={i} onClick={listValue.method}>{listValue.text}</List>
    }
  }

  render () {
    return (
      <div>
        <Bar>
          <Link to={{pathname: '/', state:{userId: this.state.userId}}}>
            <LogoContainer src={Logo} />
          </Link>
          {this.state.pages.map(this.renderNavElements.bind(this))}
        </Bar>
        <LoginPopup isOpen={this.state.isOpen} onClose={this.closeDialog.bind(this)} login={this.login.bind(this)} />
      </div>
    )
  }
}

NavBar.defaultProps = {
  userId: 0,
  redirect: function(){}
}

export default NavBar
