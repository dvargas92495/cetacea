import React from 'react'
import {Card, Icon} from '@blueprintjs/core'
import styled from 'styled-components'
import {Link} from 'react-router-dom'
import Logo from '../images/cetacea_logo.png'
import NavBar from './components/NavBar.js'

const BackgroundCard = styled(Card)`
  width: 600px;
  text-align: center;
  margin: 30px 25% 5px 25%;
  height: 150px;
  padding: 40px;
  font-family: Allerta;
  font-size: x-large;
  color: #616161;
`

class SettingsPage extends React.Component {
  constructor(props) {
    super(props)
    const userId = props.location.state ? props.location.state.userId : 0
    this.state = {
      userId: userId
    }
  }

  redirectToHome() { //TODO: I think this will go in the base page class
    this.props.history.push("/", {userId:0});
  }

  render () {
    return (
      <div>
      <NavBar redirect={this.redirectToHome.bind(this)} onlogin={this.setState.bind(this)} userId={this.state.userId}/>
        <BackgroundCard>
          {"Hello, the settings page is not yet implemented. Check back soon!"}
        </BackgroundCard>
      </div>
    )
  }
}

export default SettingsPage
