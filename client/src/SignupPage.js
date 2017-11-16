import React from 'react'
import {EditableText} from '@blueprintjs/core'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import moment from 'moment';
import {Link} from 'react-router-dom'

class SignupPage extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      pages: [ ['Learn More', '/about'], ['Sign Up', '/signup']]
    }
  }

  componentDidMount() {
    console.log('it wroked')
    gapi.signin2.render('g-signin2', {
      'scope': 'https://www.googleapis.com/auth/plus.login',
      'onsuccess': this.onSignIn
    })
  }

  onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile()
    var id_token = googleUser.getAuthResponse().id_token


    fetch('/api/login', {
      method: 'POST',
      body: JSON.stringify({
        idtoken: id_token,
        isSignup: "true"
      })
    }).then(function(resp){
      return resp.json();
    }).then(function(body){
      console.log(body)
    })

    // fetch('/api/journal?id=1').then(function(resp){
    //   return resp.json();
    // }).then(function(body){
    //   self.setState({value: body.entry})
    // });

    console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
    console.log('Name: ' + profile.getName());
    console.log('Image URL: ' + profile.getImageUrl());
    console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
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
