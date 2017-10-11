import React from 'react'
import {EditableText} from '@blueprintjs/core'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import moment from 'moment';

class JournalPage extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      pages: [ ['Help', 'http://www.cetacea.xyz/'], ['Settings', 'http://www.cetacea.xyz/'], ['Groups', 'http://www.cetacea.xyz/'], ['Journal', 'http://www.cetacea.xyz/']]
    }
  }

  render () {
    const DateHeader = styled.h1`
      display: block;
      text-align: right;
      font-size: 2em;
      margin-top: 0.75em;
      margin-bottom: 1em;
      margin-left: 0;
      margin-right: 1.5em;
      font-family: Allerta;
      color: #FFFFFF;
      font-weight: lighter;
    `
    const SmallText = styled.h3`
      display: inline-block;
      text-align: right;
      font-size: 12px;
      font-family: Allerta;
      color: #FFFFFF;
      font-weight: lighter;
    `
    const Entry = styled.textarea`
      display: block;
      margin-left: auto;
      margin-right: auto;
      margin-bottom: 20px;
      width: 95%;
      height: 400px;
      resize: none;
      outline: none;
    `
    return (
      <div>
        <NavBar pages={this.state.pages} />
        <DateHeader>
          {moment().format("dddd, MMMM D, YYYY").toString()}
        </DateHeader>
        <Entry placeholder='Start typing here.'/>
        <div style={{'textAlign':'right', 'margin-right':'10px'}}>
          <SmallText>
            last submit: 10/11/17 9:24 pm
          </SmallText>
          <Button text="submit"/>
        </div>
      </div>
    )
  }
}


export default JournalPage
