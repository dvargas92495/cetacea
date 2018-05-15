import React from 'react'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import moment from 'moment';
import {Toaster, Position} from '@blueprintjs/core'

const Entry = styled.textarea`
  display: block;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 20px;
  width: 95%;
  height: 400px;
  resize: none;
  outline: none;
  `;
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
  font-weight: lighter;`;
const SmallText = styled.h3`
  display: inline-block;
  text-align: right;
  font-size: 12px;
  font-family: Allerta;
  color: #FFFFFF;
  font-weight: lighter;
  margin-right: 15px`;

const ButtonDiv = styled.div`
  margin-right: 30px;
  margin-bottom: 20px;
  text-align: right;
`

const ToasterDiv = styled(Toaster)`
  text-align: center;
`

class JournalPage extends React.Component {
  constructor(props) {
    super(props);
    const userId = props.location.state ? props.location.state.userId : 0
    this.state = {
      value: '',
      userId: userId,
      lastSubmit: moment().toDate(),
      toaster: Toaster,
      refHandlers: {
        toaster: (ref: Toaster) => this.toaster = ref
      }
    };

    this.getJournal({userId: userId});

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);

 //    private toaster: Toaster;
 // private refHandlers = {
 //     toaster: (ref: Toaster) => this.toaster = ref,
 // };

  }



  handleErrorMessage(msg){
    console.log(msg);
  }

  handleChange(event) {
    this.setState({value: event.target.value})
  }

  handleSubmit(event) {
    var self = this;
    var timestamp = moment().toDate();
    fetch('/api/journal', {
      method: 'POST',
      body: JSON.stringify({
        user_id: this.state.userId,
        entry: this.state.value,
        timestamp: timestamp
      })
    }).then(function(){
      self.setState({lastSubmit: timestamp});
      self.toaster.show({ message: "Your journal has been saved."})
    });
  }

  getJournal(userObj){
    var self = this;
    if (userObj.userId == 0) return
    fetch('/api/journal?id='+userObj.userId).then(function(resp){
      return resp.json();
    }).then(function(body){
      if (body.isError) {
        self.handleErrorMessage(body.message);
      } else if (body.entry) {
        self.setState({
          userId: userObj.userId,
          value: body.entry,
          lastSubmit: moment(moment.utc(body.timestamp, "MMM DD, YYYY hh:mm:ss a").toDate()).format("M/D/YY hh:mm a")
        });
      }
    });
  }

  redirectToHome() { //TODO: I think this will go in the base page class
    this.props.history.push("/", {userId:0});
  }

  render() {
    return (
      <div>
        <Toaster position={Position.TOP_CENTER} ref={this.state.refHandlers.toaster} />
        <NavBar redirect={this.redirectToHome.bind(this)}  onlogin={this.getJournal.bind(this)} userId={this.state.userId}/>
        <DateHeader>
          {moment().format("dddd, MMMM D, YYYY").toString()}
        </DateHeader>
        <Entry value={this.state.value} onChange={this.handleChange} placeholder="Start writing here!"/>
        <ButtonDiv>
          <SmallText>
            {"Last submit: " + moment(this.state.lastSubmit).format("M/D/YY hh:mm a")}
          </SmallText>
          <Button text="save" press={this.handleSubmit}/>
        </ButtonDiv>
      </div>
    )
  }
}

export default JournalPage
