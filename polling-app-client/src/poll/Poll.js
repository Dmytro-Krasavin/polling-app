import React, { Component } from 'react';
import './Poll.css';
import { Avatar, Button, Icon, Radio } from 'antd';
import { Link } from 'react-router-dom';
import getAvatarColor from '../util/Colors';
import { formatDateTime } from '../util/Helpers';

const RadioGroup = Radio.Group;

class Poll extends Component {
  calculatePercentage = (choice) => {
    if (this.props.poll.totalVotes === 0) {
      return 0;
    }
    return (choice.voteCount * 100) / (this.props.poll.totalVotes);
  };

  isSelected = (choice) => {
    return this.props.poll.selectedChoice === choice.id;
  };

  getWinningChoice = () => {
    return this.props.poll.choices.reduce((prevChoice, currentChoice) =>
        currentChoice.voteCount > prevChoice.voteCount ? currentChoice : prevChoice,
      { voteCount: -Infinity }
    );
  };

  getTimeRemaining = (poll) => {
    const expirationTime = new Date(poll.expirationDateTime).getTime();
    const currentTime = new Date().getTime();

    const difference_ms = expirationTime - currentTime;
    const days = Math.floor(difference_ms / (1000 * 60 * 60 * 24));
    if (days > 0) {
      return days + ' days left';
    }

    const hours = Math.floor((difference_ms / (1000 * 60 * 60)) % 24);
    if (hours > 0) {
      return hours + ' hours left';
    }

    const minutes = Math.floor((difference_ms / 1000 / 60) % 60);
    if (minutes > 0) {
      return minutes + ' minutes left';
    }

    const seconds = Math.floor((difference_ms / 1000) % 60);
    if (seconds > 0) {
      return seconds + ' seconds left';
    }

    return 'less than a second left';
  };

  render() {
    const pollChoices = [];
    const { poll, currentVote } = this.props;
    const creator = poll.createdBy;

    if (poll.selectedChoice || poll.expired) {
      const winningChoice = poll.expired ? this.getWinningChoice() : null;

      poll.choices.forEach(choice => {
        pollChoices.push(<CompletedOrVotedPollChoice
          key={choice.id}
          choice={choice}
          isWinner={winningChoice && choice.id === winningChoice.id}
          isSelected={this.isSelected(choice)}
          percentVote={this.calculatePercentage(choice)}
        />);
      });
    } else {
      poll.choices.forEach(choice => {
        pollChoices.push(<Radio className="poll-choice-radio" key={choice.id} value={choice.id}>{choice.text}</Radio>)
      })
    }

    return (
      <div className="poll-content">
        <div className="poll-header">
          <div className="poll-creator-info">
            <Link className="creator-link" to={`/users/${creator.username}`}>
              <Avatar className="poll-creator-avatar"
                      style={{ backgroundColor: getAvatarColor(creator.name) }}>
                {creator.name[0].toUpperCase()}
              </Avatar>
              <span className="poll-creator-name">
                                {creator.name}
                            </span>
              <span className="poll-creator-username">
                                @{creator.username}
                            </span>
              <span className="poll-creation-date">
                                {formatDateTime(poll.creationDateTime)}
                            </span>
            </Link>
          </div>
          <div className="poll-question">
            {poll.question}
          </div>
        </div>
        <div className="poll-choices">
          <RadioGroup
            className="poll-choice-radio-group"
            onChange={this.props.handleVoteChange}
            value={currentVote}>
            {pollChoices}
          </RadioGroup>
        </div>
        <div className="poll-footer">
          {
            !(poll.selectedChoice || poll.expired) ?
              (<Button className="vote-button" disabled={!currentVote}
                       onClick={this.props.handleVoteSubmit}>Vote</Button>) : null
          }
          <span className="total-votes">{poll.totalVotes} votes</span>
          <span className="separator">•</span>
          <span className="time-left">
                        {
                          poll.expired ? 'Final results' :
                            this.getTimeRemaining(poll)
                        }
                    </span>
        </div>
      </div>
    );
  }
}

const CompletedOrVotedPollChoice = (props) => {
  return (
    <div className="cv-poll-choice">
            <span className="cv-poll-choice-details">
                <span className="cv-choice-percentage">
                    {Math.round(props.percentVote * 100) / 100}%
                </span>
                <span className="cv-choice-text">
                    {props.choice.text}
                </span>
              {
                props.isSelected ? (
                  <Icon
                    className="selected-choice-icon"
                    type="check-circle-o"
                  />) : null
              }
            </span>
      <span className={props.isWinner ? 'cv-choice-percent-chart winner' : 'cv-choice-percent-chart'}
            style={{ width: props.percentVote + '%' }}>
            </span>
    </div>
  );
}


export default Poll;
