import React from 'react'
import {EditableText} from '@blueprintjs/core'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import moment from 'moment';

class HomePage extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      pages: [ ['Learn More', 'http://www.cetacea.xyz/'], ['Log In', 'http://www.cetacea.xyz/']]
    }
    this.handleClick = this.handleClick.bind(this);
  }

  handleClick(e) {
    e.preventDefault()
    window.location = 'http://www.cetacea.xyz/entry';
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
          <Button text="lets go" press={this.props.onClick}/>
          <Button text="learn more" press={this.handleClick}/>
        </div>
      </div>
    )
  }
}


export default HomePage
