package com.wefika.calendar.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.joda.time.LocalDate;

/**
 * Created by Blaz Solar on 27/02/14.
 */
public class CalendarManager {

    @NonNull
    private State mState;
    @NonNull
    private RangeUnit mUnit;
    @NonNull
    private LocalDate mSelected;
    @NonNull
    private final LocalDate mToday;
    @Nullable
    private LocalDate mMinDate;
    @Nullable
    private LocalDate mMaxDate;
    @NonNull
    private Formatter formatter;
    @NonNull
    private final StateChangeListener mStateChangeListener;

    private LocalDate mActiveMonth;

    public CalendarManager(@NonNull LocalDate selected, @NonNull State state, @Nullable LocalDate minDate,
                           @Nullable LocalDate maxDate, @NonNull StateChangeListener stateChangeListener) {
        this(selected, state, minDate, maxDate, stateChangeListener, null);
    }

    private CalendarManager(@NonNull LocalDate selected, @NonNull State state, @Nullable LocalDate minDate,
                           @Nullable LocalDate maxDate, @NonNull StateChangeListener stateChangeListener, @Nullable Formatter formatter) {
        mToday = LocalDate.now();
        mState = state;
        mStateChangeListener = stateChangeListener;

        if (formatter == null) {
            this.formatter = new DefaultFormatter();
        } else {
            this.formatter = formatter;
        }

        init(selected, minDate, maxDate);
    }

    public CalendarManager copy() {
        return new CalendarManager(mSelected, mState, mMinDate, mMaxDate, mStateChangeListener);
    }

    public boolean selectDay(@NonNull LocalDate date) {
        if (!mSelected.isEqual(date)) {
            mUnit.deselect(mSelected);
            mSelected = date;
            mUnit.select(mSelected);

            if (mState == State.WEEK) {
                setActiveMonth(date);
            }
            return true;
        } else {
            return false;
        }
    }

    @NonNull
    public LocalDate getSelectedDay() {
        return mSelected;
    }

    @NonNull
    public String getHeaderText() {
        return formatter.getHeaderText(mUnit.getType(), mUnit.getFrom(), mUnit.getTo());
    }

    public boolean hasNext() {
        return mUnit.hasNext();
    }

    public boolean hasPrev() {
        return mUnit.hasPrev();
    }

    public boolean next() {

        boolean next = mUnit.next();
        mUnit.select(mSelected);

        setActiveMonth(mUnit.getFrom());

        return next;
    }

    public boolean prev() {

        boolean prev = mUnit.prev();
        mUnit.select(mSelected);

        setActiveMonth(mUnit.getTo());

        return prev;
    }

    /**
     * @return index of month to focus to
     */
    public void toggleView() {

        if (mState == State.MONTH) {
            toggleFromMonth();
        } else {
            toggleFromWeek();
        }

    }

    @NonNull
    public State getState() {
        return mState;
    }

    public void setState(@NonNull State state) {
        this.mState = state;
    }

    public CalendarUnit getUnits() {
        return mUnit;
    }

    public LocalDate getActiveMonth() {
        return mActiveMonth;
    }

    private void setActiveMonth(LocalDate activeMonth) {
        mActiveMonth = activeMonth.withDayOfMonth(1);
    }

    private void toggleFromMonth() {

        // if same month as selected
        if (mUnit.isInView(mSelected)) {
            toggleFromMonth(mSelected);

            setActiveMonth(mSelected);
        } else {
            setActiveMonth(mUnit.getFrom());
            toggleFromMonth(mUnit.getFirstDateOfCurrentMonth(mActiveMonth));
        }
    }

    void toggleToWeek(int weekInMonth) {
        LocalDate date = mUnit.getFrom().plusDays(weekInMonth * 7);
        toggleFromMonth(date);
    }

    private void toggleFromMonth(LocalDate date) {
        setUnit(new Week(date, mToday, mMinDate, mMaxDate));
        mUnit.select(mSelected);
        mState = State.WEEK;
        mStateChangeListener.onStateChanged(mState);
    }

    private void toggleFromWeek() {

        setUnit(new Month(mActiveMonth, mToday, mMinDate, mMaxDate));
        mUnit.select(mSelected);

        mState = State.MONTH;
        mStateChangeListener.onStateChanged(mState);
    }

    private void init() {
        if (mState == State.MONTH) {
            setUnit(new Month(mSelected, mToday, mMinDate, mMaxDate));
        } else {
            setUnit(new Week(mSelected, mToday, mMinDate, mMaxDate));
        }
        mUnit.select(mSelected);
    }

    void setUnit(@NonNull RangeUnit unit) {
        if (unit != null) {
            mUnit = unit;
        }
    }

    public int getWeekOfMonth() {
        if (mUnit.isInView(mSelected)) {
            if (mUnit.isIn(mSelected)) { // TODO not pretty
                return mUnit.getWeekInMonth(mSelected);
            } else if (mUnit.getFrom().isAfter(mSelected)) {
                return mUnit.getWeekInMonth(mUnit.getFrom());
            } else {
                return mUnit.getWeekInMonth(mUnit.getTo());
            }
        } else {
            return mUnit.getFirstWeek(mUnit.getFirstDateOfCurrentMonth(mActiveMonth)); // if not in this month first week should be selected
        }
    }

    public void init(@NonNull LocalDate date, @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        mSelected = date;
        setActiveMonth(date);
        mMinDate = minDate;
        mMaxDate = maxDate;

        init();
    }

    @Nullable
    public LocalDate getMinDate() {
        return mMinDate;
    }

    public void setMinDate(@Nullable LocalDate minDate) {
        mMinDate = minDate;
    }

    @Nullable
    public LocalDate getMaxDate() {
        return mMaxDate;
    }

    public void setMaxDate(@Nullable LocalDate maxDate) {
        mMaxDate = maxDate;
    }

    @NonNull
    public Formatter getFormatter() {
        return formatter;
    }

    public enum State {
        MONTH,
        WEEK
    }

    public interface StateChangeListener {
        void onStateChanged(State state);
    }
}
