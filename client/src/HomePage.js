import React from 'react'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import {Link} from 'react-router-dom'
import LoginPopup from './components/LoginPopup.js'

const Title = styled.h1`
  display: block;
  text-align: center;
  font-size: 4em;
  margin-top: 1.5em;
  margin-bottom: 0.5em;
  margin-left: 0;
  margin-right: 0;
  font-family: Allerta;
  color: #FFFFFF;
  font-weight: bold;`;
const Subtitle = styled.h2`
  display: block;
  text-align: center;
  font-size: 2em;
  margin-top: 0.5em;
  margin-bottom: 0.5em;
  margin-left: 0;
  margin-right: 0;
  font-family: Allerta;
  color: #FFFFFF;
  font-weight: normal;`;
const Space = styled.div`
  margin-right: 50px;
  display: inline-block;`;

class HomePage extends React.Component {

  constructor(props) {
    super(props);
    const userId = props.location.state ? props.location.state.userId || 0:0;
    const aboutPage = {text: 'About', path:'/about'};
    const loginPage = {text: 'Log In', method:this.openDialog.bind(this)};
    const helpPage = {text: 'Help', path:'/help', params: {userId: userId}};
    const settingsPage = {text: 'Settings', path: '/settings', params: {userId: userId}};
    const logoutPage = {text: 'Log Out', path: '/journal'};
    const groupPage = {text: 'Groups', path: '/group', params: {userId: userId}};
    const journalPage = {text: 'Journal', path: '/journal', params: {userId: userId}};
    this.loggedOutPages = [ aboutPage, loginPage];
    this.loggedInPages = [ helpPage, settingsPage, logoutPage, groupPage, journalPage];
    this.state = {
      isOpen: false,
      pages: userId > 0 ? this.loggedInPages: this.loggedOutPages,
      userId: userId
    };

    if (this.state.userId != null && this.state.userId > 0) {
      return;
    }

    var self = this;
    gapi.load('auth2', function(){
      var a2 = gapi.auth2.init({
        client_id: '548992550759-kmikahq1pkfhffgps85151j5o2a6gduu.apps.googleusercontent.com',
        scope: 'https://www.googleapis.com/auth/plus.login'
      });
      a2.then(function(){
        if(a2.isSignedIn.get()) {
          var id_token = a2.currentUser.get().getAuthResponse().id_token;
          fetch('/api/login', {
            method: 'POST',
            body: JSON.stringify({
              idtoken: id_token,
              isSignup: "false"
            })
          }).then(function(resp){
            return resp.json();
          }).then(function(body){
            self.setState({id: body.id});
            self.updateUser(body.id);
          });
        }
      })
    });
  }

  openDialog() {
    this.setState({isOpen: true })
  }

  closeDialog() {
    this.setState({isOpen: false })
  }

  updateUser(id){
    this.loggedInPages[3].params = {userId: id};
    this.loggedInPages[4].params = {userId: id};
    this.setState({
      'pages': this.loggedInPages,
      'userId': id
    });
  }

  render () {
    return (
      <div>
        <NavBar pages={this.state.pages} userId={this.state.userId}/>
        <LoginPopup isOpen={this.state.isOpen} onClose={this.closeDialog.bind(this)} updateUser={this.updateUser.bind(this)}/>
        <Title>
          welcome to cetacea
        </Title>
        <Subtitle>
          journaling for a porpoise
        </Subtitle>
        <div style={{'textAlign': "center", 'topMargin': '25px'}}>
          {this.state.userId == 0 ?
            <Link to='/signup'>
              <Button text="sign up"/>
            </Link> :
            <Link to={{pathname: "/journal", state:{userId: this.state.userId}}}>
              <Button text="let's go"/>
            </Link>
          }
          <Space/>
          <Link to = '/about'>
            <Button text="learn more" />
          </Link>
        </div>
      </div>
    )
  }
}

export default HomePage
// <div id="g-signin2" data-onsuccess={this.onSignIn}></div>
