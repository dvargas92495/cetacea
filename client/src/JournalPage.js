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
  font-weight: lighter;`;

class JournalPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pages: [ ['Help', '/'], ['Settings', '/' ], ['Groups', '/'], ['Journal', '/journal']],
      value: '',
      userId: props.match.params.userId,
      lastSubmit: ''
    };

    var self = this;
    fetch('/api/journal?id='+this.state.userId).then(function(resp){
      return resp.json();
    }).then(function(body){
      if (body.isError) {
        self.handleErrorMessage(body.message);
      } else {
        self.setState({value: body.entry});
      }
    });

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleErrorMessage(msg){
    console.log(msg);
  }

  handleChange(event) {
    this.setState({value: event.target.value})
  }

  handleSubmit(event) {
    var timestamp = moment().toDate();
    fetch('/api/journal', {
      method: 'POST',
      body: JSON.stringify({
        user_id: this.state.userId,
        entry: this.state.value,
        timestamp: timestamp
      })
    }).then(function(){

    });
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
