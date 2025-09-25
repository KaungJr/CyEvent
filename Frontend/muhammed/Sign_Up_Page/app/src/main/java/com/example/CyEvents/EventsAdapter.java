package com.example.CyEvents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Sign_up_Page.R;
import java.util.List;

/**
 * EventsAdapter is a custom RecyclerView adapter that binds event data to the RecyclerView items.
 * The adapter takes a list of Event objects and handles displaying each event's title in a view.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private List<Event> events; // List of Event objects to be displayed
    private OnItemClickListener onItemClickListener; // Listener for handling item click events

    /**
     * Interface definition for a callback to be invoked when an item in the RecyclerView is clicked.
     */
    public interface OnItemClickListener {
        /**
         * Method to be called when an event item is clicked.
         *
         * @param event The Event object that was clicked.
         * @param position The position of the clicked event in the list.
         */
        void onItemClick(Event event, int position);
    }

    /**
     * Constructor for the EventsAdapter.
     *
     * @param events A list of Event objects to be displayed in the RecyclerView.
     */
    public EventsAdapter(List<Event> events) {
        this.events = events; // Initialize the event list
    }

    /**
     * Called when a new ViewHolder needs to be created. This method creates and returns a new
     * EventViewHolder with the specified view.
     *
     * @param parent The parent view that the new ViewHolder will be added to.
     * @param viewType The view type of the new ViewHolder.
     * @return A new EventViewHolder instance.
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display the event data at a specified position. This method
     * binds the event data to the provided ViewHolder.
     *
     * @param holder The ViewHolder in which the event data should be displayed.
     * @param position The position of the event in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position); // Get the event at the current position
        holder.bind(event, onItemClickListener, position); // Bind the event data to the ViewHolder
        holder.itemView.setAlpha(0); // Set initial alpha to 0 for fade-in effect
        holder.itemView.animate().alpha(1).setDuration(500).start(); // Animate fade-in
    }

    /**
     * Returns the number of events in the adapter.
     *
     * @return The total number of events.
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * Sets the OnItemClickListener for the adapter.
     *
     * @param listener The listener to handle click events on the RecyclerView items.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener; // Set the click listener for event items
    }

    /**
     * EventViewHolder is a static inner class that extends RecyclerView.ViewHolder and is used to
     * display event data in the RecyclerView item view.
     */
    static class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventTitle; // TextView to display the event title
        private TextView eventDate; // TextView to display the event date
        private TextView eventTime; // TextView to display the event time
        private TextView eventLocation; // TextView to display the event location

        /**
         * Constructor for the EventViewHolder.
         *
         * @param itemView The view for the RecyclerView item.
         */
        public EventViewHolder(@NonNull View itemView) {
            super(itemView); // Call the superclass constructor
            eventTitle = itemView.findViewById(R.id.eventTitle); // Initialize the TextView for displaying event title
            eventDate = itemView.findViewById(R.id.eventDate); // Initialize the TextView for displaying event date
            eventTime = itemView.findViewById(R.id.eventTime); // Initialize the TextView for displaying event time
            eventLocation = itemView.findViewById(R.id.eventLocation); // Initialize the TextView for displaying event location
        }

        /**
         * Binds the event data to the ViewHolder.
         *
         * @param event The Event object to display.
         * @param listener The listener to handle click events.
         * @param position The position of the event in the list.
         */
        public void bind(Event event, OnItemClickListener listener, int position) {
            eventTitle.setText(event.getTitle()); // Set the event title in the TextView
            eventDate.setText("Date: " + event.getDate()); // Set the event date in the TextView
            eventTime.setText("Time: " + event.getTime()); // Set the event time in the TextView
            eventLocation.setText("Location: " + event.getLocation()); // Set the event location in the TextView

            itemView.setOnClickListener(v -> {
                if (listener != null) { // Check if the listener is set
                    listener.onItemClick(event, position); // Notify the listener of item click
                }
            });
        }
    }
}
