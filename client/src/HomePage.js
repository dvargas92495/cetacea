import React from 'react'
import {EditableText} from '@blueprintjs/core'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import moment from 'moment';
import {Link} from 'react-router-dom'

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
  font-weight: bold;`
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
  font-weight: normal;`

class HomePage extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      pages: [ ['Learn More', '/about'], ['Log In', '/journal']]
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
        idtoken: id_token
      })
    })

    console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
    console.log('Name: ' + profile.getName());
    console.log('Image URL: ' + profile.getImageUrl());
    console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
  }


  render () {
    return (
      <div>
        <NavBar pages={this.state.pages} />
        <Title>
          welcome to cetacea
        </Title>
        <Subtitle>
          journaling for a porpoise
        </Subtitle>
        <div id="g-signin2" data-onsuccess={this.onSignIn}></div>
        <div style={{'textAlign': "center", 'topMargin': '25px'}}>
        <Link to='/journal'>
          <Button text="lets go" />
        </Link>
        <Link to = '/about'>
          <Button text="learn more" />
        </Link>
        </div>
      </div>
    )
  }
}

export default HomePage
