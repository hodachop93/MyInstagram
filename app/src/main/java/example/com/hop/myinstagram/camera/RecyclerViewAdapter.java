package example.com.hop.myinstagram.camera;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.com.hop.myinstagram.R;

/**
 * Created by Hop on 02/07/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.FilterWayViewHolder> {
    private List<FilterWay> filterWays;

    private SparseBooleanArray selectedItems;
    private FilterWayViewHolder.RecyclerViewClickListener clickListener;


    public RecyclerViewAdapter(List<FilterWay> filterWays, FilterWayViewHolder.RecyclerViewClickListener clickListener) {
        this.filterWays = filterWays;
        selectedItems = new SparseBooleanArray();
        this.clickListener = clickListener;
    }

    /**
     * Kiểm tra phần tử tại vị trí position đã được chọn hay chưa
     *
     * @param position
     * @return true nếu được chọn và ngược lại
     */
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Lấy số phần tử được chọn
     *
     * @return số phần tử được chọn
     */
    public int getSelectedItemsCount() {
        return selectedItems.size();
    }

    /**
     * Xóa hết tất cả các tình trạng chọn trong Recycler View
     */
    public void clearSelection() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    /**
     * Chốt tình trạng được chọn vào phần tử ở vị trí position
     *
     * @param position vị trí mà ta sẽ chốt tình trạng được chọn vào
     */
    public void toogleSelection(int position) {

        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            //clearSelection();
            selectedItems.put(position, true);
        }
        //Dang ky thay doi phan tu i
        notifyItemChanged(position);

    }

    /**
     * Lấy 1 List các vị trí được chọn
     *
     * @return List các vị trí của các phần tử được chọn
     */
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FilterWayViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        FilterWayViewHolder viewHolder = new FilterWayViewHolder(v, clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterWayViewHolder filterWayViewHolder, int i) {
        //i ở đây chính là vị trí của cái View hiện tại trong RecyclerView và cũng là phần tử trong mảng data
        filterWayViewHolder.img_preview.setImageBitmap(filterWays.get(i).getBmp());
        filterWayViewHolder.title_filter.setText(filterWays.get(i).getTitle());

        //Highlight items nếu nó được chọn
        filterWayViewHolder.indicator.setVisibility(isSelected(i) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return filterWays.size();
    }

    public static class FilterWayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_preview;
        TextView title_filter;
        //RelativeLayout container;
        ImageView indicator;
        private RecyclerViewClickListener listener;

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getPosition());
            }
        }

        public interface RecyclerViewClickListener {
            public void onItemClicked(int position);
        }

        public FilterWayViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            this.img_preview = (ImageView) itemView.findViewById(R.id.img_preview);
            this.title_filter = (TextView) itemView.findViewById(R.id.title_filter);
            //this.container = (RelativeLayout) itemView.findViewById(R.id.card_view_container);
            this.indicator = (ImageView) itemView.findViewById(R.id.indicator);
            this.listener = listener;

            itemView.setOnClickListener(this);

            RelativeLayout.LayoutParams paramsImg_preview = (RelativeLayout.LayoutParams) img_preview.getLayoutParams();
            RelativeLayout.LayoutParams paramsIndicator = (RelativeLayout.LayoutParams) indicator.getLayoutParams();
            paramsIndicator.width = paramsImg_preview.width;

        }

    }
}
