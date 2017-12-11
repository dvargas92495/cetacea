import React from 'react'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import moment from 'moment';
import {Card} from '@blueprintjs/core'

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

const BackdropCard = styled(Card)`
  margin: 20px 100px 20px 100px;
  height: 100vh;
  opacity: 0.95;
  overflow: scroll;
`
const Title = styled.h1`
  font-family: Allerta;
  text-align: center;
  margin-top: 10px;`

const Question = styled.li`
  font-family: Allerta;
  text-align: left;
  font-size: 2em;
`
const Answer = styled.p`
  margin: 10px 0px 25px 0px;
  text-indent: 15px;
`
const OL = styled.ol`
  padding-right: 30px;
  margin-bottom: 50px;
`

const UL = styled.ul`
  padding-right: 30px;
  margin-bottom: 50px;
  list-style: none;
`

const Issue = styled.li`
  font-size: 20px;
  font-family: Allerta;
`

class HelpPage extends React.Component {
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
        <BackdropCard>
          <Title>
            {"Getting Started"}
          </Title>
          <hr/>
          <Answer>{"Congratulations on your new Cetacea account! We hope you enjoy the experience and have put together a quick starter guide so you can get comfortable with the website."}</Answer>
          <OL>
            <Question>{"Making an account."}</Question>
            <Answer>{"You've already completed this step, just by signing up with your Google Account. As of " + moment().format("L") + ", a Google account is the only way to sign in and use Cetacea. If you ever have any trouble regarding your Google account, please keep in mind that you must go through Google's processes to reset passwords and remember usernames."}</Answer>
            <Question>{"Join or make a group."}</Question>
            <Answer>{"To really enjoy the benefits of Cetacea, it is important to be a part of a group. Generally, you should try to form groups with acquaintances, friends, or family that you already know or feel comfortable with."}</Answer>
            <Answer>{"Join an existing group: As of " + moment().format("L") + " it is currently impossible to join any existing groups without admin involvement."}</Answer>
            <Answer>{"Make group: You can create a new group by navigating to the Groups tab in the top navigation bar (www.cetacea.xyz/group), and pressing the New Group button on the left side of the screen. As of " + moment().format("L") + ", this feature has not been fully released."}</Answer>
            <Question>{"Writing a journal."}</Question>
            <Answer>{"Writing journals are at the core of Cetacea. You can start writing a journal by pressing the “Journal” tab in the top navigation bar after logging in (www.cetacea.xyz/journal)."}</Answer>
            <Answer>{"Cetacea's default reset time is 5am, meaning that at 5am each day, your last saved journal is sent out to your group, your journal entry page is reset, and anything you subsequently write will be sent out in the next day's email. However, if it is between 5am and 4:59am the next day, your in-progress journals will be viewable from the Journals tab."}</Answer>
            <Answer>{"To save a journal, simply press the submit button at the bottom right of the page. Submitting a new journal will overwrite your last saved journal, so do not erase the auto populated journal entry as you add more to the entry. There is currently no autosave function, so make sure to save frequently!"}</Answer>
            <Question>{"Receive the daily email."}</Question>
            <Answer>{"Every day, cetacea will bundle up all of your group member's entries from the previous 24 hours and will send an email to the entire group. If you are in multiple groups, you will receive multiple emails. If you are not receiving emails, first make sure that you are checking the Google Account that you signed in with, and then check your spam/junk folder. If any of the Cetacea emails are marked as spam or junk, use Google's settings to unflag the emails for the future."}</Answer>
          </OL>
          <Title>
            {"Detailed Help"}
          </Title>
          <hr/>
          <Answer>{"In this section we will have more detailed help regarding specific components of Cetacea. As of" + moment().format("L") + ", not every feature is implemented so some sections may be empty or incomplete."}</Answer>
          <UL>
            <Question>{"Accounts"}</Question>
            <UL>
              <Issue>{"Help, I'm locked out of my account."}</Issue>
              <Answer>{"If you are not able to use your username and password to log into Cetacea, please check your Google Account settings."}</Answer>
              <Issue>{"How do I upload a picture for my account?"}</Issue>
              <Answer>{"As of " + moment().format("L") + "this feature is not yet implemented."}</Answer>
            </UL>
            <Question>{"Journaling"}</Question>
            <UL>
              <Issue>{"What happens when I save a journal?"}</Issue>
              <Answer>{"Every time you press the submit button, your current journal entry, the one you see on your webpage, will overwrite your previously saved entry. If you just want to update your last entry, please make sure to edit or add on to the entry, and not delete and start over or all previous work will be lost."}</Answer>
              <Issue>{"I'm confused about timezones and Cetacea's 24-hour period."}</Issue>
              <Answer>{"As of " + moment().format("L") + "we have no features regarding timezones. Please check back again after this feature is implemented."}</Answer>
            </UL>
            <Question>{"Groups"}</Question>
            <UL>
              <Issue>{"What are the group membership models?"}</Issue>
              <Answer>{"Group membership models exist to help moderate how new members join an existing group. Some preliminary membership models are below:"}</Answer>
              <ul>
                <li>{"All Members Approve: All members must approve a new member before they can join the group."} </li>
                <li>{"Group Owner Approves: Only the group owner (usually the group creator) can approve a new member."} </li>
                <li>{"Any Member Approves: Any member can approve a new member."} </li>
              </ul>
              <Answer>{"More membership models may be available in the future."}</Answer>
              <Issue>{"What happens when I add a member?"}</Issue>
              <Answer>{"As of "+ moment().format("L") + ", nothing! Check back soon after this feature is implemented."}</Answer>
              <Issue>{"What is email frequency?"}</Issue>
              <Answer>{"Some groups may choose to have less frequent emails, for example once a week instead of once a day. This is an option for those groups, though we recommend daily emails for the optimal Cetacea experience."}</Answer>
              <Issue>{"What does \"email send time\" mean?"}</Issue>
              <Answer>{"Email send time is when your cetacea group resets. The default time for a group is 5am (after most people have gone to sleep and before most people wake up), but this option gives groups the ability to customize their journal reset time and when the group email gets sent out."}</Answer>
            </UL>
          </UL>
          <Title>
            {"Contact Us"}
          </Title>
          <hr/>
          <Answer>
            {"Have a question or found a bug? Let us know by emailing "}
            <a href="mailto:cetacea@mit.edu?Subject=Cetacea%20Bug">{"cetacea@mit.edu"}</a>
            {" with details."}
          </Answer>
        </BackdropCard>
      </div>
    )
  }
}

export default HelpPage
