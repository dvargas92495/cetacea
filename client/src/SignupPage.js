import React from 'react'
import {EditableText} from '@blueprintjs/core'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import moment from 'moment';
import {Link} from 'react-router-dom'

class SignupPage extends React.Component {

  constructor(props) {
    super(props);
    const aboutPage = {text: 'Learn More', path:'/about'};
    const homePage = {text: 'Home', path: '/'};
    this.state = {
      pages: [ aboutPage, homePage]
    }
  }

  componentDidMount() {
    gapi.signin2.render('g-signin2', {
      'scope': 'https://www.googleapis.com/auth/plus.login',
      'onsuccess': this.onSignIn.bind(this)
    });
  }

  onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile();
    var id_token = googleUser.getAuthResponse().id_token;

    var self = this;
    fetch('/api/login', {
      method: 'POST',
      body: JSON.stringify({
        idtoken: id_token,
        isSignup: "true"
      })
    }).then(function(resp){
      return resp.json();
    }).then(function(body){
      self.props.history.push("/", {userId:body.id});
    })
  }


  render () {
    return (
      <div>
        <NavBar pages={this.state.pages} />
        <div id="g-signin2" data-onsuccess={this.onSignIn}></div>
        <div style={{'textAlign': "center", 'topMargin': '25px'}}>
        </div>
      </div>
    )
  }
}

export default SignupPage
