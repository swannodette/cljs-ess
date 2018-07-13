import React from 'react';
import ReactDOM from 'react-dom';
import DatePicker from 'react-datepicker';
import moment from 'moment';
import 'react-datepicker/dist/react-datepicker.css';
window.moment = {moment: moment};
window.React = React;
window.ReactDOM = ReactDOM;
window.ReactDatePicker = {DatePicker: DatePicker};