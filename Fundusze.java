package getting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.html.HTMLElement;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUListElement;

public class Fundusze extends JFrame {

	final String html = "http://www.pkotfi.pl";
	private Properties management;
	private JTextField nazwa;
	private JTextField link;
	private JTextField wartosc;
	private JList lista;
	private JScrollPane skroll;
	private int interwa³;
	private JSlider slider;
	private HashMap<String, String> nazwaWartosc;
	private Entry<String, String> entryMap;
	private Set<String> secik;
	Iterator<String> iSecik;
	private JFrame jf = new JFrame();

	private JButton pokaz;
	DefaultListModel<String> dlm;
	List<String> listaVal = new LinkedList<String>();

	@SuppressWarnings("unchecked")
	public Fundusze() throws Exception {
		setTitle("Fundusze");
		setLayout(null);

		link = new JTextField();
		link.setToolTipText("wpisz link do strony");
		link.setBounds(50, 20, 250, 20);
		link.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent action) {
				Object source = action.getSource();
				
			}
			
		});
		add(link);

		nazwa = new JTextField();
		nazwa.setBounds(50, 45, 250, 20);
		add(nazwa);

		wartosc = new JTextField();
		wartosc.setBounds(50, 70, 250, 20);
		add(wartosc);

		slider = new JSlider(0, 3000, 0);
		slider.setBounds(50, 120, 200, 20);
		slider.setMajorTickSpacing(500);
		slider.setMinorTickSpacing(500);
		// slider.setPaintLabels(true);
		// slider.setPaintTicks(true);
		slider.setMaximum(3000);
		slider.setMinimum(1000);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent suw) {
				interwa³ = slider.getValue();
			}
		});
		add(slider);

		pokaz = new JButton("Pokaz");
		pokaz.setBounds(350, 110, 120, 30);
		pokaz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				Object o = ev.getSource();

				if (o == pokaz) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							secik = nazwaWartosc.keySet();
							iSecik = secik.iterator();
							try {
								for (int i = 0; i < secik.size(); i++) {
									Thread.sleep(interwa³);
									nazwa.setText(iSecik.next());
									// try {

									wartosc.setText(nazwaWartosc.get(nazwa.getText()));

								}
							} catch (Exception e) {
							}

						}
					}).start();

				}

			}

		});

		add(pokaz);

		lista = new JList<String>();
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dlm = new DefaultListModel<String>();
		lista.addListSelectionListener(new ListSelectionListener() {

			Document doc = Jsoup.connect(html).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0)")
					.get();

			Elements tableElements = doc.select("table");

			Elements tableRowElements = tableElements.select("tr");
			List<Element> lel = new ArrayList<Element>();

			{
				for (int i = 1; i < tableRowElements.size(); i++) {
					Element row = tableRowElements.get(i);
					// System.out.println(row);
					lel.add(row);
				}
			}

			{
				nazwaWartosc = new LinkedHashMap();
				// Pattern p = Pattern.compile(name);

				for (Element rowy : lel) {
					String nazwa = rowy.select("td:eq(0)").text();
					String wartosc = rowy.select("td:eq(1)").text();
					nazwaWartosc.put(nazwa, wartosc);
					listaVal.add(wartosc);
					dlm.addElement(nazwa);

				}

				for (Entry<String, String> entryMap : nazwaWartosc.entrySet()) {

					System.out.println(entryMap.getKey() + "...." + entryMap.getValue());
				}
			}

			@Override
			public void valueChanged(ListSelectionEvent le) {
				{
					nazwa.setText(lista.getSelectedValue().toString());
					wartosc.setText(listaVal.get(lista.getSelectedIndex()));
				}

			}

		});
		// lista.addListSelectionListener(this);
		lista.setModel(dlm);
		add(lista);
		skroll = new JScrollPane(lista);
		skroll.setBounds(310, 20, 300, 70);
		add(skroll);
	}

	public static void main(String[] args) throws Exception {
		Fundusze f = new Fundusze();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(true);
		f.setLocation(509, 100);
		f.setSize(700, 300);
		f.setVisible(true);
	}

}
