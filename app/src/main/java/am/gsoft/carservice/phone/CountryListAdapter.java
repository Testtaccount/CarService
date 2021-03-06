package am.gsoft.carservice.phone;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public final class CountryListAdapter extends
    ArrayAdapter<CountryInfo> implements SectionIndexer {
  private final HashMap<String, Integer> alphaIndex = new LinkedHashMap<>();
  private final HashMap<String, Integer> countryPosition = new LinkedHashMap<>();
  private String[] sections;

  public CountryListAdapter(Context context) {
    super(context, com.firebase.ui.auth.R.layout.fui_dgts_country_row, android.R.id.text1);
  }

  // The list of countries should be sorted using locale-sensitive string comparison
  public void setData(List< CountryInfo> countries) {
    // Create index and add entries to adapter
    int index = 0;
    for ( CountryInfo countryInfo : countries) {
      final String key = countryInfo.locale.getDisplayCountry().substring(0, 1).toUpperCase
          (Locale.getDefault());

      if (!alphaIndex.containsKey(key)) {
        alphaIndex.put(key, index);
      }
      countryPosition.put(countryInfo.locale.getDisplayCountry(), index);

      index++;
      add(countryInfo);
    }

    sections = new String[alphaIndex.size()];
    alphaIndex.keySet().toArray(sections);

    notifyDataSetChanged();
  }

  @Override
  public Object[] getSections() {
    return sections;
  }

  @Override
  public int getPositionForSection(int index) {
    if (sections == null) {
      return 0;
    }

    // Check index bounds
    if (index <= 0) {
      return 0;
    }
    if (index >= sections.length) {
      index = sections.length - 1;
    }

    // Return the position
    return alphaIndex.get(sections[index]);
  }

  @Override
  public int getSectionForPosition(int position) {
    return 0;
  }

  public int getPositionForCountry(String country) {
    final Integer position = countryPosition.get(country);
    return position == null ? 0 : position;
  }
}
