import React from 'react'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import moment from 'moment';
import {Dialog} from '@blueprintjs/core'
import LoginPopup from './components/LoginPopup.js'

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
  font-weight: lighter;`
const SmallText = styled.h3`
  display: inline-block;
  text-align: right;
  font-size: 12px;
  font-family: Allerta;
  color: #FFFFFF;
  font-weight: lighter;`
const Entry = styled.textarea`
  display: block;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 20px;
  width: 95%;
  height: 400px;
  resize: none;
  outline: none;`

class AboutPage extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      pages: [ ['Help', '/'], ['Settings', '/' ], ['Groups', '/'], ['Journal', '/journal']],
      isOpen: false
    }
    this.toggleDialog = this.toggleDialog.bind(this)

  }


  toggleDialog() {
    this.setState({isOpen: !this.state.isOpen })
  }

  render () {
    return (
      <div>
        <NavBar pages={this.state.pages} />
        <DateHeader>
          {moment().format("dddd, MMMM D, YYYY").toString()}
        </DateHeader>
        <Button press={this.toggleDialog}/>
        <LoginPopup isOpen={this.state.isOpen} onClose={this.toggleDialog} />
      </div>
    )
  }
}


export default AboutPage
