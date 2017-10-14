import React from 'react'
import {EditableText} from '@blueprintjs/core'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import moment from 'moment';
import {Link} from 'react-router-dom'

class HomePage extends React.Component {

  constructor(props) {
    console.log('home')
    super(props)
    this.state = {
      pages: [ ['Learn More', '/about'], ['Log In', '/journal']]
    }
    this.handleClick = this.handleClick.bind(this);
  }

  handleClick(e) {
    e.preventDefault()
    return (
      <Link to='journal'/>
    )
  }

  render () {
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
      font-weight: bold;
    `
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
      font-weight: normal;
    `

    return (
      <div>
        <NavBar pages={this.state.pages} />
        <Title>
          welcome to cetacea
        </Title>
        <Subtitle>
          journaling for a porpoise
        </Subtitle>
        <div style={{'textAlign': "center", 'margin-top': '25px'}}>
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
