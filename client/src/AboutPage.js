import React from 'react'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import moment from 'moment';
import {Dialog} from '@blueprintjs/core'
import LoginPopup from './components/LoginPopup.js'
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
const OL = styled.ul`
  padding-right: 30px;
  list-style: none;
`

class AboutPage extends React.Component {
  constructor(props) {
    super(props)
    const userId = props.location.state.userId;
    this.state = {
      userId: userId
    }
  }

  render () {
    return (
      <div>
        <NavBar/>
        <BackdropCard>
          <Title>
            {"About Cetacea"}
          </Title>
          <hr/>
          <OL>
            <Question> {"What is Cetacea?"} </Question>
            <Answer>{"Cetacea is a journal sharing app meant to help communities stay connected and to encourage insightful reflection through writing. Our app allows people to keep up with their friends by writing journal entries for their groups to view through once-a-day emails. This way, journal readers will understand what’s going on with their friends while journal writers will have a chance to explain their thoughts and feelings in a productive way."}</Answer>
            <Question> {"What does the name \"Cetacea\" mean?"} </Question>
            <Answer>{"Cetaceans are an order of aquatic mammals that include whales, dolphins, and porpoises. They are known for their excellent communication skills and, in many species, their close knit pods. We wanted our users to emulate these amazing animals since our product aims to increase quality of communication and share knowledge between group members."}</Answer>
            <Question> {"What inspired Cetacea?"} </Question>
            <Answer>{"We had a positive experience journaling for a summer internship and wanted to bring journaling back to MIT, our university, where students are often too busy and stressed to reflect on their daily experiences and thoughts. We also wanted to have a deeper understanding of how our friends were doing, since many interactions never got beyond a “whats up?” or “how are you?”. The prevalent but superficial social media presence in our friendships also bothered us, and we had a desire for more meaningful relationships with our friends. Thus, Cetacea was born. "}</Answer>
            <Question>{"Prototyping Cetacea"}</Question>
            <Answer>{"While we were in the early stages of coding Cetacea, we created a prototype using a simple script and Google Form to test our concept on real people. Two groups were formed in September 2017— around 15 people each from each of our college living groups — to see what people thought about sharing daily journals with their friends. The groups were a huge success, and inspired us to continue our work on Cetacea."}</Answer>
            <Question>{"Who can use Cetacea?"}</Question>
            <Answer>{"Anyone can! While Cetacea was inspired by MIT students and their social communities, any demographic can benefit from using Cetacea. Sports teams, companies, families, long distance friends, sororities, labs, or even book clubs are all great groups to try Cetacea with."}</Answer>
            </OL>

        </BackdropCard>
      </div>
    )
  }
}

export default AboutPage
