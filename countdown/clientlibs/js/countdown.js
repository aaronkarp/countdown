function countdownStart () {

  // Read in all elements with data-endDate attributes
  let countdowns = document.querySelectorAll("[data-enddate]");

  // Pull in configuration for each countdown from data attributes in HTML
  Array.prototype.slice.call(countdowns).forEach(function (countdown) {
    var endDate = countdown.dataset.enddate;
    let color = countdown.dataset.color;
    let fontfamily = countdown.dataset.fontfamily;
    let unitsday = countdown.dataset.unitsday;
    let unitshour = countdown.dataset.unitshour;
    let unitsminute = countdown.dataset.unitsminute;
    let unitssecond = countdown.dataset.unitssecond;
    let countdownId = '#' + countdown.dataset.countdownid;
    let separator = countdown.dataset.separator;
    let labelday = countdown.dataset.labelday;
    let labelhour = countdown.dataset.labelhour;
    let labelminute = countdown.dataset.labelminute;
    let labelsecond = countdown.dataset.labelsecond;

    // Add classes to the countdown's div to apply selected styles

    if (fontfamily != '' && fontfamily != null) {
      // Add the selected font class to the countdown's parent div
      $(countdownId).addClass('countdown-' + fontfamily);
    }

    if (color != '' && color != null) {
      // Add the color to the countdown's parent div as an inline style
      $(countdownId).css('color', color);
    }

    // Construct countdown string format to match author's settings

    var countdownString = '<strong>';

    if (unitsday != 'false') {
      countdownString += '<span class="days">%d</span>';
      if (labelday != '' && labelday != null) {
        countdownString += '<span class="timerText">' + labelday + '</span>';
      }
    }

    if (unitshour != "false") {
      if ((unitsday != 'false') && (separator != '' && separator != null)) {
        countdownString += ' ' + separator + ' ';
      }
      countdownString += '<span class="hours">%H</span>'
      if (labelhour != '' && labelhour != null) {
        countdownString += '<span class="timerText">' + labelhour + '</span>';
      }
    }

    if (unitsminute != "false") {
      if (((unitsday != 'false') || unitshour != 'false') && (separator != '' && separator != null)) {
        countdownString += ' ' + separator + ' ';
      }
      countdownString += '<span class="minutes">%M</span>'
      if (labelminute != '' && labelminute != null) {
        countdownString += '<span class="timerText">' + labelminute + '</span>';
      }
    }

    if (unitssecond != "false") {
      if (((unitsday != 'false') || unitshour != 'false' || unitsminute != 'false') && (separator != '' && separator != null)) {
        countdownString += ' ' + separator + ' ';
      }
      countdownString += '<span class="seconds">%S</span>'
      if (labelsecond != '' && labelsecond != null) {
        countdownString += '<span class="timerText">' + labelsecond + '</span>';
      }
    }

    countdownString += '</strong>';

    $(countdownId).countdown(endDate, function (event) {
      // Calculate total hours remaining if days are not displayed
      if (unitsday == "false") {;
        let totalHours = event.offset.totalDays * 24 + event.offset.hours;
        if (totalHours < 10) {
          totalHours = '0' + totalHours;
        }
        countdownString = countdownString.replace('%H', totalHours);
      }

      // Remove extraneous whitespace to prevent odd spacing on display
      countdownString = countdownString.replace('</span> <span', '</span><span');
      $(this).html(
        event.strftime(countdownString)
      )
    })
  });
}