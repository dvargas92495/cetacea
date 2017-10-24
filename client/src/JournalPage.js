import React from 'react'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import moment from 'moment';

const Entry = styled.textarea`
  display: block;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 20px;
  width: 95%;
  height: 400px;
  resize: none;
  outline: none;`
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

class JournalPage extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      pages: [ ['Help', '/'], ['Settings', '/' ], ['Groups', '/'], ['Journal', '/journal']],
      value: ''
    }

    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleChange(event) {
    this.setState({value: event.target.value})
  }

  handleSubmit(event) {
    fetch('/journal', {
      method: 'POST',
      body: {
        user_id: '1',
        entry: this.state.value,
        timestamp: moment().toDate(),
      }
    })
  }

  render() {
    return (
      <div>
        <NavBar pages={this.state.pages} />
        <DateHeader>
          {moment().format("dddd, MMMM D, YYYY").toString()}
        </DateHeader>
        <Entry value={this.state.value} onChange={this.handleChange} placeholder="Start writing here!"/>
        <div style={{'textAlign':'right', 'marginRight':'10px'}}>
          <SmallText>
            {"Last submit: " + moment().format("M/D/YY h:m a")}
          </SmallText>
          <Button text="submit" press={this.handleSubmit}/>
        </div>
      </div>
    )
  }
}

export default JournalPage
